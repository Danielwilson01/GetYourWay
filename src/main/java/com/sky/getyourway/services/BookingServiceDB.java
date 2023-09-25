package com.sky.getyourway.services;

import com.sky.getyourway.domain.Booking;
import com.sky.getyourway.dtos.BookingDTO;
import com.sky.getyourway.repo.BookingRepo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class BookingServiceDB implements BookingService {

    private BookingRepo repo;

    // Constructor injecting the repo
    public BookingServiceDB(BookingRepo repo) {
        this.repo = repo;
    }

    @Override
    public Booking addBooking(Booking b) {
        return this.repo.save(b);
    }

    @Override
    public Booking getBookingById(Integer id) {
        Optional<Booking> found = this.repo.findById(id);
        return found.get();
    }

    @Override
    public Booking getBookingByOrderReference(String orderReference) {
        Optional<Booking> found = Optional.ofNullable(this.repo.findByOrderReference(orderReference));
        return found.get();
    }


    @Override
    public String cancelBooking(Integer id) {
        if(this.repo.existsById(id)) {
            this.repo.deleteById(id);
            return "Booking with id " + id + " removed";
        } else {
            return  "NOT FOUND";
        }
    }

    @Override
    public Booking findBookingByOrderReference(String orderReference) {
        return this.repo.findByOrderReference(orderReference);
    }

    @Override
    public List<BookingDTO> getBookings() {
        List<BookingDTO> dtos = new ArrayList<>();

        for (Booking b: this.repo.findAll()) {
            dtos.add(new BookingDTO(b));
        }
        return dtos;

    }
}
