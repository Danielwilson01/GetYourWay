package com.sky.getyourway.rest;

import com.sky.getyourway.domain.Customer;
import com.sky.getyourway.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


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

    // TODO : getCustomer passing in an ID
    // TODO : getAll (however user wont use it, might be useful for testing purposes
    // TODO : update (shall we have separate one for email?)
    // TODO : deleteCustomer passing in an ID (might give the option to users to remove their profiles in future?)
    // TODO : run Postman requests and write tests


}
