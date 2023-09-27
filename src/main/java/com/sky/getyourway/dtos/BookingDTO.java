package com.sky.getyourway.dtos;

import com.sky.getyourway.domain.Booking;
import com.sky.getyourway.domain.User;

import java.awt.print.Book;

public class BookingDTO {

    private int id;

    private String orderReference;

    private Integer customerId;

    // CONSTRUCTORS
    public BookingDTO(Booking b) {
        this.id = b.getId();
        this.orderReference = b.getOrderReference();
        if (b.getCustomer() != null) {
            this.customerId = b.getCustomer().getId();
        }
    }

    public BookingDTO() {
        super();
    }

    public BookingDTO(String orderReference, User user) {
    }


    // GETTERS & SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
