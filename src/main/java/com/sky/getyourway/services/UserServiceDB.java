package com.sky.getyourway.services;

import com.sky.getyourway.domain.User;
import com.sky.getyourway.dtos.UserDTO;
import com.sky.getyourway.exception.EmailInUseException;
import com.sky.getyourway.repo.UserRepo;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class UserServiceDB implements UserService {

    private UserRepo repo;

    private BCryptPasswordEncoder encoder;

    // Constructor injecting the repo
    public UserServiceDB(UserRepo repo, BCryptPasswordEncoder encoder) {
        super();
        this.repo = repo;
        this.encoder = encoder;
    }


    /* createCustomer(): adds new user to our DB
      @params c user object
      @return user added */
    @Override
    public User createCustomer(User user) {
        if (isEmailInUse(user.getEmail())) {
            throw new EmailInUseException("Email is already in use");
        }
        user.setPassword(this.encoder.encode(user.getPassword()));
        return this.repo.save(user);
    }

    /* isEmailInUse(): checks if the email is already in our DB
     @params email
     @return true/false  */
    public boolean isEmailInUse(String email) {
        // returns TRUE if an email is found in the repo, otherwise returns FALSE
        return this.repo.findByEmailIgnoreCase(email).isPresent();
    }

    /* getUser(): gets a user given an ID
     @params id user ID
     @return user corresponding to that ID  */
    @Override
    public User getUser(Integer id) {
        // Using Optional<> as it is possible we find no user with that id
        Optional<User> found = this.repo.findById(id);
        return found.get();
    }

    /* getAll(): gets all users in our DB
    @return List of users  */
    @Override
    public List<UserDTO> getAll() {
        List<UserDTO> dtos = new ArrayList<>();
        for (User u: this.repo.findAll()) {
            dtos.add(new UserDTO(u));
        }
        return dtos;
    }

    /* updateUser(): updates the user data
     @params id (user ID), firstName, lastName, email, passwordCurrent, passwordNew
     @return user for which details were updated with the updated details
     NOTE: password parameters are to check if the user has inserted the correct password before updating*/
    @Override
    public User updateUser(Integer id, String firstName, String lastName, String email, String passwordCurrent, String passwordNew) {
        User toUpdate = this.getUser(id);  // gets the user to be updated

        if (firstName != null) toUpdate.setFirstName(firstName);
        if (lastName != null) toUpdate.setLastName(lastName);
        if (email != null) toUpdate.setEmail(email);
        if (passwordCurrent != null && passwordNew != null && this.encoder.matches(passwordCurrent, toUpdate.getPassword())) {
                toUpdate.setPassword(this.encoder.encode(passwordNew));
        }

        System.out.println(toUpdate);
        return this.repo.save(toUpdate);
    }

    /* findCustomerByEmail(): given an email, it will return the user for that email
    @params email
    @return user corresponding to that email */
    @Override
    public User findCustomerByEmail(String email) {

        return this.repo.findByEmailIgnoreCase(email).get();
    }

    /* removeUser(): removes the user from our DB
    @params id user ID
    @return string confirming removal or failure to do so */
    @Override
    public String removeUser(int id) {
        if (this.repo.existsById(id)) {
            this.repo.deleteById(id);
            return "User with id " + id + " removed.";
        } else {
            return  "NOT FOUND";
            }
        }
}



