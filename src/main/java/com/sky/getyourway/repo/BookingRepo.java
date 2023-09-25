package com.sky.getyourway.repo;

import com.sky.getyourway.domain.Booking;
import com.sky.getyourway.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {

    // Method to be used to find a booking searching by orderReference in DB
    Booking findByOrderReference(String orderReference);

}
