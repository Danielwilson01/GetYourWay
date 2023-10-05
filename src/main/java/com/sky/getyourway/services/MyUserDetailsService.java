package com.sky.getyourway.services;

import com.sky.getyourway.domain.MyUserDetails;
import com.sky.getyourway.domain.User;
import com.sky.getyourway.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User found = this.repo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user exists with name: " + email));
        return new MyUserDetails(found);
    }
}
