package com.example.demo.handler;

import com.example.demo.dao.CustomerDao;
import com.example.demo.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class CustomerHandler {
    private final CustomerDao customerDao;

    @Autowired
    public CustomerHandler(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public Mono<ServerResponse> loadCustomers(ServerRequest request) {
        return ServerResponse.ok().body(customerDao.getCustomersFunctional(), Customer.class);
    }

    public Mono<ServerResponse> findCustomer(ServerRequest request) {
        int customerId = Integer.parseInt(request.pathVariable("input"));
        Mono<Customer> customer = customerDao.getCustomersFunctional()
//                .filter(c -> c.getId().equals(customerId)).take(1).single();
                .filter(c -> c.getId().equals(customerId)).next();
        return ServerResponse.ok().body(customer, Customer.class);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        Mono<Customer> customer = request.bodyToMono(Customer.class);
        Mono<String> customerString = customer.map(dto -> dto.getName() + " " + dto.getId());
        return ServerResponse.ok().body(customerString, String.class);
    }

}
