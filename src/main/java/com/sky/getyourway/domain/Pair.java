package com.sky.getyourway.domain;

import com.sky.getyourway.dtos.OfferDTO;

public class Pair {

    private String offerId;
    private String currency;
    private Integer price;
    private OfferDTO outboundFlight;
    private OfferDTO inboundFlight;

    public Pair() {}

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
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
