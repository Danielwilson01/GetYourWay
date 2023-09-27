package com.sky.getyourway.services;

import com.sky.getyourway.domain.Booking;
import com.sky.getyourway.domain.User;
import com.sky.getyourway.repo.BookingRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookingServicesDBUnitTest {

    @Autowired
    private BookingService service;

    @MockBean
    private BookingRepo repo;

    @Test
    void testAddBooking(){
        Integer id = 4;

        User user = new User(id, "john", "cena", "cena@email", "blue");
        Booking addBooking = new Booking("ord_0000ABcdef", user);

        Mockito.when(this.repo.save(addBooking)).thenReturn(addBooking);

        Assertions.assertEquals(addBooking, this.service.addBooking(addBooking));

    }

    // TODO: GET booking by ID
    // TODO: get booking by order reference
    // TODO: GET ALL BOOKINGS?
    // TODO: Get Booking by Order reference
    // TODO: Cancel Booking
}
