package com.sky.getyourway.domain;

import com.sky.getyourway.dtos.OfferDTO;

import java.math.BigDecimal;
import java.util.List;

public class Pair {

    private List<String> passengerIds;
    private List<String> passengersType;
    private String offerId;
    private String currency;
    private BigDecimal price;
    private OfferDTO outboundFlight;
    private OfferDTO inboundFlight;

    public Pair() {}

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
}
