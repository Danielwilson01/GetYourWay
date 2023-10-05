package com.sky.getyourway.repo;

import com.sky.getyourway.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    // Method to be used to find a user/customer ID given an email
    Optional<User> findByEmailIgnoreCase(String email);
}
