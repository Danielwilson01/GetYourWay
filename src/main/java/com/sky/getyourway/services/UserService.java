package com.sky.getyourway.services;

import com.sky.getyourway.domain.User;

import java.util.List;

public interface UserService {

    User createUser(User u);
    User getUser(int id);
    List<User> getAll();
    User updateUser(
            Integer id,
            String firstName,
            String lastName,
            String email,
            String password
    );

    String removeUser(int id);
}
