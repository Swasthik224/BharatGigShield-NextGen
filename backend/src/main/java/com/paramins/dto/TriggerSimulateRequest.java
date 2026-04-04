package com.paramins.dto;
import java.time.LocalDate;
public class TriggerSimulateRequest {
    private String city, riskType;
    private LocalDate eventDate;
    private double observedValue;

    public String getCity()                 { return city; }
    public void setCity(String v)           { this.city = v; }
    public String getRiskType()             { return riskType; }
    public void setRiskType(String v)       { this.riskType = v; }
    public LocalDate getEventDate()         { return eventDate; }
    public void setEventDate(LocalDate v)   { this.eventDate = v; }
    public double getObservedValue()        { return observedValue; }
    public void setObservedValue(double v)  { this.observedValue = v; }
}
