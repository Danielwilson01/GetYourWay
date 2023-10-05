package com.sky.getyourway.rest;

import com.sky.getyourway.domain.User;
import com.sky.getyourway.dtos.UserDTO;
import com.sky.getyourway.exception.EmailInUseException;
import com.sky.getyourway.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/*Constroller for the User class*/


@RestController
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        super();
        this.service = service;
    }

    @GetMapping("/hello")
    public String httpTest(){
        return "Yep, you're ON!";
    }

    @PostMapping("/register")
    // When user creates a new account and submits its data, a new user is created in our DB
    public ResponseEntity<User> registerCustomer(@RequestBody User c) {
        // Creates and adds the user to DB + responds to client with an HTTP status of created
        // NOTE: the try/catch is checking that the email  used to create new customer is NOT already assigned to other users


        c.setEmail(c.getEmail().toLowerCase());
        return new ResponseEntity<>(this.service.createCustomer(c), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginCustomer(@RequestBody Map<String, String> loginData) {

        User found = this.service.findCustomerByEmail(loginData.get("email").toLowerCase());
        if (found == null) {
            // If no customer is found with that email, return a NOT FOUND status
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!found.getPassword().equals(loginData.get("password"))) {
            // If a customer is found by email but the password doesn't match, return status UNAUTHORIZED
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        // If a customer is found by email and password matches the stored password, return Status OK and customer
        return ResponseEntity.ok(found);
    }

    @GetMapping("/getAll")
    public List<UserDTO> getUser() {
        return this.service.getAll();
    }

    @PatchMapping("/update")
    public ResponseEntity<User> updateUser(
            @RequestParam(name = "id", required = true ) Integer id,
            @RequestParam(name = "firstName", required = false) String firstName,
            @RequestParam(name = "lastName", required = false) String lastName,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "passwordCurrent", required = false) String passwordCurrent,
            @RequestParam(name = "passwordNew", required = false) String passwordNew) {

        User updated = this.service.updateUser(id, firstName, lastName, email, passwordCurrent, passwordNew);
        if (updated == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeUser(@PathVariable int id) {
        String result = this.service.removeUser(id);
        if ("NOT FOUND".equalsIgnoreCase(result)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return ResponseEntity.ok(result);
    }

    @GetMapping("/user")
    public User getCurrent() {
        return this.service.findCustomerByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
