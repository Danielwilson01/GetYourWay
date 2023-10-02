package com.sky.getyourway.dtos;

import java.util.List;

/*
* OfferDTO is to represent the information extracted from the Duffle API on a search
* Searches can be for direct/connection flights, one-way/return, etc.
* */

public class OfferDTO {

    // *******ATTRIBUTES*******
    private String departingAt;
    private String arrivingAt;
    private Integer stops;
    private List<String> airportStopCode;
    private List<String> airportStopName;
    private List<String> airline;
    private List<String> flightNumber;
    private String origin;
    private String destination;
    private Double destLong;
    private Double destLat;
    private String duration;

    // *******CONSTRUCTORS*******
    public OfferDTO() {

    }

    // *******SETTERS & GETTERS*******
    public void setAirportStopName(List<String> airportStopName) {
        this.airportStopName = airportStopName;
    }

    public List<String> getAirportStopName() {
        return airportStopName;
    }

    public List<String> getAirportStopCode() {
        return airportStopCode;
    }

    public void setAirportStopCode(List<String> airportStopCode) {
        this.airportStopCode = airportStopCode;
    }

    public String getDepartingAt() {
        return departingAt;
    }

    public void setDepartingAt(String departingAt) {
        this.departingAt = departingAt;
    }

    public String getArrivingAt() {
        return arrivingAt;
    }

    public void setArrivingAt(String arrivingAt) {
        this.arrivingAt = arrivingAt;
    }

    public Integer getStops() {
        return stops;
    }

    public void setStops(Integer stops) {
        this.stops = stops;
    }

    public List<String> getAirline() {
        return airline;
    }

    public void setAirline(List<String> airline) {
        this.airline = airline;
    }

    public List<String> getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(List<String> flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Double getDestLong() {
        return destLong;
    }

    public void setDestLong(Double destLong) {
        this.destLong = destLong;
    }

    public Double getDestLat() {
        return destLat;
    }

    public void setDestLat(Double destLat) {
        this.destLat = destLat;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
