package com.sky.getyourway.rest;

import com.sky.getyourway.domain.Customer;
import com.sky.getyourway.exception.EmailInUseException;
import com.sky.getyourway.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class CustomerController {

    private CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    public String httpTest(){
        return "Yep, you're ON!";
    }

    @PostMapping("/register")
    // When user creates a new account and submits its data, a new user is created in our DB
    public ResponseEntity<Customer> registerCustomer(@RequestBody Customer c) {
        // Creates and adds the user to DB + responds to client with an HTTP status of created
        // NOTE: the try/catch is checking that the email  used to create new customer is NOT already assigned to other users

        try {
            c.setEmail(c.getEmail().toLowerCase());
            return new ResponseEntity<>(this.service.createCustomer(c), HttpStatus.CREATED);
        } catch (EmailInUseException eiue) {
            return new ResponseEntity<>(HttpStatus.IM_USED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/login")
    public ResponseEntity<Customer> loginCustomer(
        @RequestParam(name = "email") String email,
        @RequestParam(name = "password") String password) {

        System.out.println("EMAIL: " + email);
        System.out.println("password: " + password);

        Customer found = this.service.findCustomerByEmail(email.toLowerCase());
        if (found == null) {
            // If no customer is found with that email, return a NOT FOUND status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!found.getPassword().equals(password)) {
            // If a customer is found by email but the password doesn't match, return status UNAUTHORIZED
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // If a customer is found by email and password matches the stored password, return Status OK and customer
        return ResponseEntity.ok(found);
    }

    @GetMapping("/getAll")
    public List<Customer> getCustomers() {
        return this.service.getAll();
    }

    @PatchMapping("/update")
    public ResponseEntity<Customer> updateCustomer(
            @RequestParam(name = "id", required = true ) Integer id,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "passwordCurrent", required = false) String passwordCurrent,
            @RequestParam(name = "passwordNew", required = false) String passwordNew) {

        Customer updated = this.service.updateCustomer(id, firstName, lastName, email, passwordCurrent, passwordNew);
        if (updated == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeCustomer(@PathVariable int id) {
        String result = this.service.removeCustomer(id);
        if ("NOT FOUND".equalsIgnoreCase(result)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return ResponseEntity.ok(result);
    }
}
