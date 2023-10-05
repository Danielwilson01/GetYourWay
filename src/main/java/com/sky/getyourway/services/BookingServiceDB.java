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

    /* addBooking(): adds new booking to the DB
      @params b Booking to be added
      @return booking added */
    @Override
    public Booking addBooking(Booking b) {
        return this.repo.save(b);
    }

    /* getBookingById(): gets a booking given an id
      @params id booking ID
      @return booking with that ID */
    @Override
    public Booking getBookingById(Integer id) {
        Optional<Booking> found = this.repo.findById(id);
        return found.get();
    }

    /* cancelBooking(): cancels a booking in our DB given an id
  @params id booking ID
  @return String confirming cancellation or failure to do so*/
    @Override
    public String cancelBooking(Integer id) {
        if(this.repo.existsById(id)) {
            this.repo.deleteById(id);
            return "Booking with id " + id + " removed";
        } else {
            return  "NOT FOUND";
        }
    }

    /* findBookingByOrderReference(): searches a booking by order reference returning the booking
      @params orderReference Duffle order reference
      @return booking corresponding to that reference */
    @Override
    public Booking findBookingByOrderReference(String orderReference) {
        Optional<Booking> found = Optional.ofNullable(this.repo.findByOrderReference(orderReference));
        return found.get();
    }

    /* getAllBookings(): gets all bookings in our DB
    @return List of all Bookings in our DB */
    @Override
    public List<BookingDTO> getAllBookings() {
        List<BookingDTO> dtos = new ArrayList<>();

        for (Booking b: this.repo.findAll()) {
            dtos.add(new BookingDTO(b));
        }
        return dtos;

    }

    /* getBookingsByUserID(): gets all bookings in our DB that correspond to the given user ID (Foreign key)
    @return List of all Bookings for that user in our DB */
    @Override
    public List<BookingDTO> getBookingsByUserID(int userId) {
        List<BookingDTO> dtos = new ArrayList<>();

        for (Booking b: this.repo.findAll()) {
            if(b.getCustomer().getId().equals(userId)) {
                dtos.add(new BookingDTO(b));
            }
        }

        return dtos;
    }


}
