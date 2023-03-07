package com.example.demo.service;

import com.example.demo.dao.CustomerDao;
import com.example.demo.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    @Autowired
    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> loadAllCustomer() {
        long currentTime = System.currentTimeMillis();
        List<Customer> customer = customerDao.getCustomers();
        System.out.println("Time Diff " + (System.currentTimeMillis() - currentTime));
        return customer;
    }

    public Flux<Customer> loadAllCustomerReactive() {
        long currentTime = System.currentTimeMillis();
        Flux<Customer> customer = customerDao.getCustomersReactive();
        System.out.println("Time Diff " + (System.currentTimeMillis() - currentTime));
        return customer;
    }
}
