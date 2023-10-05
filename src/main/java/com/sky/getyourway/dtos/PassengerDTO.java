package com.sky.getyourway.dtos;

/*
* PassengerDTO represent a passenger to be added to a Duffle Order before executing the order.
* Passengers will be the travellers for the trip, during the offer generation with Duffle we set the
* number of passengers of each type to be travelling but not their details. When a user selects an
* offer to proceed with, they will be required to provide all the below details so they can be added
* to the final order.
* */

public class PassengerDTO {

    // *******ATTRIBUTES*******
    private String email;
    private String givenName;
    private String familyName;
    private String title;
    private String dob;
    private String passengerId;
    private String phoneNumber;
    private String gender;
    private String infantId;


    // *******CONSTRUCTORS*******
    PassengerDTO() {}


    // *******SETTERS & GETTERS*******
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getInfantId() {
        return infantId;
    }

    public void setInfantId(String infantId) {
        this.infantId = infantId;
    }
}
