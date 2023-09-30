package com.sky.getyourway.rest;

import com.sky.getyourway.domain.Booking;
import com.sky.getyourway.dtos.BookingDTO;
import com.sky.getyourway.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* Controller for the Booking class
* NOTE: the add and remove actions are used within the SearchController for bookings to be added when oder is executed and or cancelled
* */

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
    public ResponseEntity<List<BookingDTO>> getBookingsByUserID(@PathVariable int userId) {
        List<BookingDTO> result = this.service.getBookingsByUserID(userId);
        if (result.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return ResponseEntity.ok(result);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BookingDTO>>  getAllBookings() {
        List<BookingDTO> result = this.service.getAllBookings();
        if (result.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return ResponseEntity.ok(result);
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> removeBooking(@PathVariable int id) {
        String result = this.service.cancelBooking(id);
        if ("NOT FOUND".equalsIgnoreCase(result)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return ResponseEntity.ok(result);
    }

}
