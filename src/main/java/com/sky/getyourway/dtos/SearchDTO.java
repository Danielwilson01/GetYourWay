package com.sky.getyourway.dtos;

import java.time.LocalDate;
import java.util.Map;

public class SearchDTO {

    private String airportTo;
    private String airportFrom;
    private String departureDate; // yyyy-mm-dd
    private String returnDate; // yyyy-mm-dd
    private Map<String, Integer> passengers;
    private int connections;
    private String cabinClass;

    public String getAirportTo() {
        return airportTo;
    }

    public void setAirportTo(String airportTo) {
        this.airportTo = airportTo;
    }

    public String getAirportFrom() {
        return airportFrom;
    }

    public void setAirportFrom(String airportFrom) {
        this.airportFrom = airportFrom;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public Map<String, Integer> getPassengers() {
        return passengers;
    }

    public void setPassengers(Map<String, Integer> passengers) {
        this.passengers = passengers;
    }

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }
}
