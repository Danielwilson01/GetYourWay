package com.sky.getyourway.services;

import com.sky.getyourway.domain.User;
import com.sky.getyourway.dtos.UserDTO;

import java.util.List;

public interface UserService {

    User createCustomer(User u) throws Exception;
    User getUser(Integer id);
    List<UserDTO> getAll();
    User updateUser(
            Integer id,
            String firstName,
            String lastName,
            String email,
            String passwordCurrent,
            String passwordNew
    );

    User findCustomerByEmail(String email);

    String removeUser(int id);
}
