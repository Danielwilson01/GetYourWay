package com.sky.getyourway.services;

import com.sky.getyourway.domain.Customer;

import java.util.List;

public interface CustomerService {

    Customer createCustomer(Customer u);
    Customer getUser(int id);
    List<Customer> getAll();
    Customer updateCustomer(
            Integer id,
            String firstName,
            String lastName,
            String email,
            String password
    );

    String removeCustomer(int id);
}
