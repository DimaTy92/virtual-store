package com.example.demo.service;

import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void saveCustomerTest() {
        Customer customer = Customer.builder()
                .setId(1L)
                .setFirstName("Serhii")
                .setLastName("T")
                .setPhoneNumber("777")
                .build();

        customerService.registerCustomer(customer);

        Mockito.verify(customerRepository, Mockito.times(1)).save(customer);
    }
}
