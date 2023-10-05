package com.sky.getyourway.domain;

import javax.persistence.*;
import java.util.Objects;

/*
Class representing Booking as stored in MySQL database. This object/table will contain:
- id: unique identifier within our system
- order reference: Duffle order identifier.
- userID: id from the USER table identifying the Sky user making the booking (foreign key)

NOTE: Duffle order ID has not been used as primary key for this offer because as we are using testing mode we had
no assurance of orders generated in testing mode being uniques.
In real world application, order reference could have been used as primary key
 */

@Entity
public class Booking {

    // *******ATTRIBUTES*******
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // User unique identifier. @GeneratedValues annotates the if as auto_increment

    private String orderReference;  // Duffel order reference

    @ManyToOne
    private User customer;  // Foreign Key

    // *******CONSTRUCTORS*******
    public Booking() {
        super();
    }

    public Booking(String orderReference, User customer) {
        this.orderReference = orderReference;
        this.customer = customer;
    }

    public Booking(String orderReference) {
        this.orderReference = orderReference;
        this.customer = customer;
    }

    // *******SETTERS & GETTERS*******
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

    // *******EQUALS & HASHCODE*******
    // Methods needed for testing
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

    // *******TO STRING*******
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", orderReference='" + orderReference + '\'' +
                '}';
    }
}
