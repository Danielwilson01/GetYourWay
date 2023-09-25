package com.sky.getyourway.services;

import com.sky.getyourway.domain.User;
import com.sky.getyourway.exception.EmailInUseException;
import com.sky.getyourway.repo.UserRepo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class UserServiceDB implements UserService {

    private UserRepo repo;

    // Constructor injecting the repo
    public UserServiceDB(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public User createCustomer(User c) {
        if (isEmailInUse(c.getEmail())) {
            throw new EmailInUseException("Email is already in use");
        }
        return this.repo.save(c);
    }

    public boolean isEmailInUse(String email) {
        // returns TRUE if an email is found in the repo, otherwise returns FALSE
        return this.repo.findByEmailIgnoreCase(email) != null;
    }

    @Override
    public User getUser(Integer id) {
        // Using Optional<> as it is possible we find no user with that id
        Optional<User> found = this.repo.findById(id);
        return found.get();
    }

    @Override
    public List<User> getAll() {
        return this.repo.findAll();
    }

    @Override
    public User updateUser(Integer id, String firstName, String lastName, String email, String passwordCurrent, String passwordNew) {
        User toUpdate = this.getUser(id);  // gets the user to be updated

        if (firstName != null) toUpdate.setFirstName(firstName);
        if (lastName != null) toUpdate.setLastName(lastName);
        if (email != null) toUpdate.setEmail(email);
        if (passwordCurrent != null && passwordNew != null && passwordCurrent.equals(toUpdate.getPassword())) {
                toUpdate.setPassword(passwordNew);
        }

        System.out.println(toUpdate);
        return this.repo.save(toUpdate);
    }

    @Override
    public User findCustomerByEmail(String email) {
        return this.repo.findByEmailIgnoreCase(email);
    }

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



