package com.paramins.client;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class ImdApiClient {

    /** Production: GET https://api.imd.gov.in/weather?city={city}&param=rain,temp */
    public double getRainfall24h(String city) {
        double raw;
        switch (city.toLowerCase()) {
            case "mumbai":  raw = 10 + new Random().nextDouble() * 80; break;
            case "chennai": raw =  8 + new Random().nextDouble() * 60; break;
            case "kolkata": raw =  5 + new Random().nextDouble() * 70; break;
            default:        raw =      new Random().nextDouble() * 30; break;
        }
        return Math.round(raw * 10.0) / 10.0;
    }

    public double getMaxTemperature(String city) {
        double raw;
        switch (city.toLowerCase()) {
            case "delhi":   raw = 35 + new Random().nextDouble() * 12; break;
            case "chennai": raw = 32 + new Random().nextDouble() * 10; break;
            default:        raw = 28 + new Random().nextDouble() * 10; break;
        }
        return Math.round(raw * 10.0) / 10.0;
    }
}
