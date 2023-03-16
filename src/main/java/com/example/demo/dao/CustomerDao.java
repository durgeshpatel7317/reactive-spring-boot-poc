package com.example.demo.dao;

import com.example.demo.dto.Customer;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class CustomerDao {

    private static void sleepExecution(int i) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Customer> getCustomers() {
        return IntStream.rangeClosed(1, 10)
            .peek(CustomerDao::sleepExecution)
            .mapToObj(i -> new Customer(i, "Customer: " + i))
            .collect(Collectors.toList());
    }

    // Reactive approach for getting the customers
    public Flux<Customer> getCustomersReactive() {
        return Flux.range(1, 10)
            .delayElements(Duration.of(1, ChronoUnit.SECONDS))
            .map(i -> new Customer(i, "Customer: " + i));
    }

    public Flux<Customer> getCustomersFunctional() {
        return Flux.range(1, 50)
            .map(i -> new Customer(i, "Customer: " + i));
    }
}
