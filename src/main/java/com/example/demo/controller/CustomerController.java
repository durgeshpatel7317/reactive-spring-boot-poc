package com.example.demo.controller;

import com.example.demo.dto.Customer;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Traditional approach for API
    @GetMapping("/")
    public List<Customer> getAllCustomers() {
        return customerService.loadAllCustomer();
    }

    // Here including the media type as TEXT_EVENT_STREAM_VALUE
    // It will return the response for the completed events
    // Means as in when an event is completed the response is returned
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Customer> getAllCustomersReactive() {
        return customerService.loadAllCustomerReactive();
    }
}
