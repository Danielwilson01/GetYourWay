package com.sky.getyourway.rest;

import com.sky.getyourway.domain.Booking;
import com.sky.getyourway.dtos.BookingDTO;
import com.sky.getyourway.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    public BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<Booking> addBooking(@RequestBody Booking b) {
        return new ResponseEntity<>(this.service.addBooking(b), HttpStatus.CREATED);
    }

    @GetMapping("/getBooking/{userId}")
    public List<BookingDTO> getBookings(@PathVariable int userId) {
        List<BookingDTO>allBookings = new ArrayList<>();
        allBookings = this.service.getBookings();
        List<BookingDTO>results = new ArrayList<>();
        for (BookingDTO booking : allBookings) {
            if (booking.getCustomerId().equals(userId)) {
                results.add(booking);
            }
        }
        return results;
    }


    @GetMapping("/getAll")
    public List<BookingDTO> getBookings() {
        return this.service.getBookings();
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> removeBooking(@PathVariable int id) {
        String result = this.service.cancelBooking(id);
        if ("NOT FOUND".equalsIgnoreCase(result)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return ResponseEntity.ok(result);
    }

}
