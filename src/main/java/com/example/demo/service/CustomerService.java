package com.example.demo.service;

import com.example.demo.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer registerCustomer(Customer customer);

    List<Customer> findAllCustomer();
}
