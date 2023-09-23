package com.sky.getyourway.rest;

import com.sky.getyourway.domain.User;
import com.sky.getyourway.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController  {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    public String httpTest(){
        return "Yep, you're ON!";
    }

    @PostMapping("/register")
    // When user creates a new account and submits its data, a new user is created in our DB
    public ResponseEntity<User> createPerson(@RequestBody User p) {
        // Creates and adds the user to DB + responds to client with an HTTP status of created
        return new ResponseEntity<>(this.service.createUser(p), HttpStatus.CREATED);
    }

    // TODO : getUser passing in an ID
    // TODO : getAll (however user wont use it, might be useful for testing purposes
    // TODO : update (shall we have separate one for email?)
    // TODO : deleteUser passing in an ID (might give the option to users to remove their profiles in future?)
    // TODO : run Postman requests and write tests


}
