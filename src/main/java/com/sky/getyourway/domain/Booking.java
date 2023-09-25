package com.sky.getyourway.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String orderReference;  // Duffel order reference

    @ManyToOne
    private User customer;

    public int getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id == booking.id && Objects.equals(orderReference, booking.orderReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderReference);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", orderReference='" + orderReference + '\'' +
                '}';
    }
}
