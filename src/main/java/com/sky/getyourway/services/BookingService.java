package com.sky.getyourway.services;

import com.sky.getyourway.domain.Booking;
import com.sky.getyourway.repo.BookingRepo;

public interface BookingService {

    Booking addBooking(Booking b);

    Booking getBookingById(Integer id);

    Booking getBookingByOrderReference(String orderReference);

    // TODO: get the user ID searching by orderRef

    String cancelBooking(Integer id);

    Booking findBookingByOrderReference(String orderReference);

}
