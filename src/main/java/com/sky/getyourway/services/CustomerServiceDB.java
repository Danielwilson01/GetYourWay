package com.sky.getyourway.services;

import com.sky.getyourway.domain.Customer;
import com.sky.getyourway.repo.CustomerRepo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class CustomerServiceDB implements CustomerService {

    private CustomerRepo repo;

    // Constructor injecting the repo
    public CustomerServiceDB(CustomerRepo repo) {
        this.repo = repo;
    }

    @Override
    public Customer createCustomer(Customer c) {
        return this.repo.save(c);
    }

    @Override
    public Customer getCustomer(int id) {
        // Using Optional<> as it is possible we find no user with that id
        Optional<Customer> found = this.repo.findById(id);
        return found.get();
    }

    @Override
    public List<Customer> getAll() {
        return this.repo.findAll();
    }

    @Override
    public Customer updateCustomer(Integer id, String firstName, String lastName, String email, String password) {
        Customer toUpdate = this.getCustomer(id);  // gets the user to be updated

        if (firstName != null) toUpdate.setFirstName(firstName);
        if (lastName != null) toUpdate.setLastName(lastName);
        if (email != null) toUpdate.setEmail(email);
        if (password != null) toUpdate.setPassword(password);

        return this.repo.save(toUpdate);
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        return this.repo.findByEmail(email);
    }

    @Override
    public String removeCustomer(int id) {
        if (this.repo.existsById(id)) {
            this.repo.deleteById(id);
            return "Customer with id " + id + " removed.";
        } else {
            return "Customer with id " + id + " NOT FOUND.";
            }
        }
}



