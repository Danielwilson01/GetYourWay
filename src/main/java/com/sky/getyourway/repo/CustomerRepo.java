package com.sky.getyourway.repo;

import com.sky.getyourway.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    // Method to be used to find a user/customer ID given an email
    Customer findByEmailIgnoreCase(String email);
}
