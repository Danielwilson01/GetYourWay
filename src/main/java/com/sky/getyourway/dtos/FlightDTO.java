package com.sky.getyourway.dtos;


import java.util.List;

// TODO to be used to retrieve the order details to show on the profile
public class FlightDTO {

    private String cost;
    private List<String> journeyTo;
    private List<String> journeyBack;
    private List<String> passengers;

    public FlightDTO() {}

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public List<String> getJourneyTo() {
        return journeyTo;
    }

    public void setJourneyTo(List<String> journeyTo) {
        this.journeyTo = journeyTo;
    }

    public List<String> getJourneyBack() {
        return journeyBack;
    }

    public void setJourneyBack(List<String> journeyBack) {
        this.journeyBack = journeyBack;
    }

    public List<String> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<String> passengers) {
        this.passengers = passengers;
    }
}
