package com.sky.getyourway.dtos;

import com.sky.getyourway.domain.Booking;
import com.sky.getyourway.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private List<BookingDTO> bookings;

    // CONSTRUCTOR
    public UserDTO(User u) {
        this.id = u.getId();
        this.firstName = u.getFirstName();
        this.lastName = u.getLastName();
        this.email = u.getEmail();
        this.password = u.getPassword();
        List<BookingDTO> dtos = new ArrayList<>();
        for (Booking b: u.getBookings()) {
            dtos.add(new BookingDTO(b));
        this.bookings = dtos;
        }
    }

    public UserDTO() {
        super();
    }

    // GETTERS & SETTERS


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BookingDTO> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingDTO> bookings) {
        this.bookings = bookings;
    }
}
