package com.sky.getyourway.dtos;

import java.util.List;

/*
* OrderDTO represents the executed and confirmed (paid) order made by a user
* on a selected offer returned from Duffle API
* */

public class OrderDTO {

    // *******ATTRIBUTES*******
    private String offerId;
    private List<PassengerDTO> passengersDetails;
    private String currency;
    private String price;


    // *******CONSTRUCTORS*******
    public OrderDTO() {}

    public String getCurrency() {
        return currency;
    }

    // *******SETTERS & GETTERS*******
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public List<PassengerDTO> getPassengersDetails() {
        return passengersDetails;
    }

    public void setPassengersDetails(List<PassengerDTO> passengersDetails) {
        this.passengersDetails = passengersDetails;
    }
}
