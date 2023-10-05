package com.sky.getyourway.services;

import com.sky.getyourway.domain.User;
import com.sky.getyourway.dtos.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserService {

    User createCustomer(User u);
    User getUser(Integer id);
    List<UserDTO> getAll();
    User updateUser(Map<String, String> updates);

    User findCustomerByEmail(String email);

    String removeUser(int id);
}
