package com.paramins.service;

import com.paramins.client.CpcbApiClient;
import com.paramins.client.ImdApiClient;
import com.paramins.entity.Policy;
import com.paramins.entity.TriggerEvent;
import com.paramins.repository.PolicyRepository;
import com.paramins.repository.TriggerEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TriggerEngineService {

    private static final Logger log = LoggerFactory.getLogger(TriggerEngineService.class);

    private final CpcbApiClient           cpcbClient;
    private final ImdApiClient            imdClient;
    private final TriggerEventRepository  triggerRepo;
    private final PolicyRepository        policyRepo;
    private final ClaimEngineService      claimEngine;

    @Value("${trigger.aqi-threshold:300}")
    private double aqiThreshold;

    @Value("${trigger.rain-threshold-mm:50.0}")
    private double rainThreshold;

    @Value("${trigger.temp-threshold-celsius:42.0}")
    private double tempThreshold;

    public TriggerEngineService(CpcbApiClient cpcbClient, ImdApiClient imdClient,
                                 TriggerEventRepository triggerRepo,
                                 PolicyRepository policyRepo, ClaimEngineService claimEngine) {
        this.cpcbClient  = cpcbClient; this.imdClient   = imdClient;
        this.triggerRepo = triggerRepo; this.policyRepo   = policyRepo;
        this.claimEngine = claimEngine;
    }

    /** Runs every 15 minutes in production. */
    @Scheduled(cron = "${trigger.check-cron}")
    public void runTriggerChecks() {
        log.info("Trigger check running for date {}", LocalDate.now());
        List<String> cities = policyRepo.findDistinctActiveCities();
        for (String city : cities) {
            checkAqi(city);
            checkRain(city);
            checkTemp(city);
        }
    }

    @Transactional
    public void checkAqi(String city) {
        double aqi = cpcbClient.getCurrentAqi(city);
        log.debug("[AQI] {} = {}", city, aqi);
        if (aqi > aqiThreshold) {
            fire(city, Policy.RiskType.AQI, aqi, aqiThreshold, "CPCB");
        }
    }

    @Transactional
    public void checkRain(String city) {
        double rain = imdClient.getRainfall24h(city);
        log.debug("[RAIN] {} = {}mm", city, rain);
        if (rain > rainThreshold) {
            fire(city, Policy.RiskType.RAIN, rain, rainThreshold, "IMD_RAIN");
        }
    }

    @Transactional
    public void checkTemp(String city) {
        double temp = imdClient.getMaxTemperature(city);
        log.debug("[TEMP] {} = {}°C", city, temp);
        if (temp > tempThreshold) {
            fire(city, Policy.RiskType.HEAT, temp, tempThreshold, "IMD_TEMP");
        }
    }

    /** Dev/test only — fire a trigger manually from the admin API. */
    @Transactional
    public TriggerEvent manualTrigger(String city, Policy.RiskType type, double value) {
        LocalDate today = LocalDate.now();
        // Remove existing trigger for today so we can re-fire
        triggerRepo.findRecentDays(1).stream()
            .filter(t -> t.getCity().equals(city)
                && t.getRiskType() == type
                && t.getEventDate().equals(today))
            .forEach(t -> triggerRepo.deleteById(t.getId()));

        double threshold;
        switch (type) {
            case AQI:  threshold = aqiThreshold;  break;
            case RAIN: threshold = rainThreshold; break;
            default:   threshold = tempThreshold; break;
        }
        fire(city, type, value, threshold, "MANUAL");

        return triggerRepo.findRecentDays(1).stream()
            .filter(t -> t.getCity().equals(city) && t.getRiskType() == type)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Trigger not created"));
    }

    private void fire(String city, Policy.RiskType type,
                       double value, double threshold, String source) {
        LocalDate today = LocalDate.now();
        if (triggerRepo.existsByCityAndRiskTypeAndEventDate(city, type, today)) {
            log.debug("Trigger already exists for {} {} {}", city, type, today);
            return;
        }
        TriggerEvent event = TriggerEvent.builder()
            .city(city).riskType(type).eventDate(today)
            .observedValue(BigDecimal.valueOf(value))
            .thresholdValue(BigDecimal.valueOf(threshold))
            .dataSource(source).isValidated(true)
            .build();
        TriggerEvent saved = triggerRepo.save(event);
        log.info("TRIGGER FIRED: {} {} {} = {} (threshold {})", city, type, today, value, threshold);
        claimEngine.processTriggeredClaims(saved);
    }
}
