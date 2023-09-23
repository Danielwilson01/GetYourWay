package com.sky.getyourway.services;

import com.sky.getyourway.domain.User;
import com.sky.getyourway.repo.UserRepo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class UserServiceDB implements UserService{

    private UserRepo repo;

    // Constructor injecting the repo
    public UserServiceDB(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public User createUser(User u) {
        return this.repo.save(u);
    }

    @Override
    public User getUser(int id) {
        // Using Optional<> as it is possible we find no user with that id
        Optional<User> found = this.repo.findById(id);
        return found.get();
    }

    @Override
    public List<User> getAll() {
        return this.repo.findAll();
    }

    @Override
    public User updateUser(Integer id, String firstName, String lastName, String email, String password) {
        User toUpdate = this.getUser(id);  // gets the user to be updated

        if (firstName != null) toUpdate.setFirstName(firstName);
        if (lastName != null) toUpdate.setLastName(lastName);
        if (email != null) toUpdate.setEmail(email);
        if (password != null) toUpdate.setPassword(password);

        return this.repo.save(toUpdate);
    }

    @Override
    public String removeUser(int id) {
        if (this.repo.existsById(id)) {
            this.repo.deleteById(id);
            return "User with id " + id + " removed.";
        } else {
            return "User with id " + id + " NOT FOUND.";
            }
        }
}



