package com.sky.getyourway.domain;

import com.sky.getyourway.dtos.OfferDTO;

import java.math.BigDecimal;
import java.util.List;

/*
Pair class is used to represent the offers for inbound/outbound from the
Duffle API when a user makes a request for return flights
 */


public class Pair {

    // *******ATTRIBUTES*******

    // passengerIds is a list of unique Duffle Ids assigned to every individual passenger to be travelling in this order
    private List<String> passengerIds;
    private List<String> passengersType;  // adult / child / infant
    private String offerId;  // Unique Duffel ID referring to the offer for inbound/outbound
    private String currency;  //  GBP used for project
    private BigDecimal price;
    private OfferDTO outboundFlight;
    private OfferDTO inboundFlight;
    private String cabinClass;

    // *******CONSTRUCTORS*******
    public Pair() {}


    // *******SETTERS & GETTERS*******
    public List<String> getPassengersType() {
        return passengersType;
    }

    public void setPassengersType(List<String> passengersType) {
        this.passengersType = passengersType;
    }

    public List<String> getPassengerIds() {
        return passengerIds;
    }

    public void setPassengerIds(List<String> passengerIds) {
        this.passengerIds = passengerIds;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OfferDTO getOutboundFlight() {
        return outboundFlight;
    }

    public void setOutboundFlight(OfferDTO outboundFlight) {
        this.outboundFlight = outboundFlight;
    }

    public OfferDTO getInboundFlight() {
        return inboundFlight;
    }

    public void setInboundFlight(OfferDTO inboundFlight) {
        this.inboundFlight = inboundFlight;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }
}
