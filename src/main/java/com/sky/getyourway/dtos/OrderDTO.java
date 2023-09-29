package com.sky.getyourway.dtos;

import java.util.List;

public class OrderDTO {

    private String offerId;
    private List<PassengerDTO> passengersDetails;
    private String currency;
    private String price;
    private Integer userId;

    public OrderDTO() {}

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCurrency() {
        return currency;
    }

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
