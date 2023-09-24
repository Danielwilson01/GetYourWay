package com.sky.getyourway.rest;

import com.sky.getyourway.domain.Customer;
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
        return new ResponseEntity<>(this.service.createCustomer(c), HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<Customer> loginCustomer(
        @RequestParam(name = "email") String email,
        @RequestParam(name = "password") String password) {

        System.out.println("EMAIL: " + email);
        System.out.println("password: " + password);

        Customer found = this.service.findCustomerByEmail(email);
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

    // TODO : getCustomer passing in an ID
    // TODO : getAll (however user wont use it, might be useful for testing purposes)
    @GetMapping("/getAll")
    public List<Customer> getCustomers() {
        return this.service.getAll();
    }

    // TODO : update (shall we have separate one for email ?)
    // TODO : deleteCustomer passing in an ID (might give the option to users to remove their profiles in future?)
    // TODO : run Postman requests and write tests


}
