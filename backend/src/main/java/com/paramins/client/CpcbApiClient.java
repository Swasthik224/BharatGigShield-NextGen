package com.paramins.client;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Random;

@Component
public class CpcbApiClient {

    private static final Map<String, double[]> AQI_RANGES = Map.of(
        "delhi",   new double[]{180, 450},
        "mumbai",  new double[]{80,  250},
        "chennai", new double[]{60,  200},
        "kolkata", new double[]{120, 380}
    );

    /** Production: GET https://api.cpcb.gov.in/aqi?city={city}&apiKey={key} */
    public double getCurrentAqi(String city) {
        double[] range = AQI_RANGES.getOrDefault(city.toLowerCase(), new double[]{100, 300});
        double raw = range[0] + new Random().nextDouble() * (range[1] - range[0]);
        return Math.round(raw * 10.0) / 10.0;
    }
}
