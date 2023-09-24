package com.sky.getyourway.services;

import com.sky.getyourway.domain.Customer;
import com.sky.getyourway.repo.CustomerRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomerServicesDBUnitTest {

    @Autowired
    private CustomerService service;

    @MockBean
    private CustomerRepo repo;

    @Test
    void testCustomerEmailUpdate() {
        Integer id = 4;

        Optional<Customer> found = Optional.of(
                new Customer(id, "john", "cena", "cena@email", "blue"));
        Customer updated = new Customer(id, "john", "cena", "john@email", "blue");

        Mockito.when(this.repo.findById(id)).thenReturn(found);
        Mockito.when(this.repo.save(updated)).thenReturn(updated);

        Assertions.assertEquals(updated,
                this.service.updateCustomer(id, "john", "cena", "john@email", "blue", "blue"));

        Mockito.verify(this.repo, Mockito.times(1)).findById(id);
        Mockito.verify(this.repo, Mockito.times(1)).save(updated);
    }

    @Test
    void testCustomerPasswordUpdate() {
        Integer id = 4;

        Optional<Customer> found = Optional.of(
                new Customer(id, "john", "cena", "cena@email", "blue"));
        Customer updated = new Customer(id, "john", "cena", "cena@email", "red");

        Mockito.when(this.repo.findById(id)).thenReturn(found);
        Mockito.when(this.repo.save(updated)).thenReturn(updated);

        Assertions.assertEquals(updated,
                this.service.updateCustomer(id, "john", "cena", "cena@email", "blue", "red"));

        Mockito.verify(this.repo, Mockito.times(1)).findById(id);
        Mockito.verify(this.repo, Mockito.times(1)).save(updated);
    }
}
