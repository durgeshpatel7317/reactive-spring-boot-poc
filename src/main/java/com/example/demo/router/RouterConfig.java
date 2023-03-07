package com.example.demo.router;

import com.example.demo.handler.CustomerHandler;
import com.example.demo.handler.CustomerStreamHandler;
import com.example.demo.handler.ProductHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    private final CustomerHandler customerHandler;
    private final CustomerStreamHandler customerStreamHandler;
    private final ProductHandler productHandler;

    @Autowired
    public RouterConfig(CustomerHandler customerHandler, CustomerStreamHandler customerStreamHandler, ProductHandler productHandler) {
        this.customerHandler = customerHandler;
        this.customerStreamHandler = customerStreamHandler;
        this.productHandler = productHandler;
    }

    @Bean
    // A Replacement of RestController and RequestMapping
    public RouterFunction<ServerResponse> customerRouteFunction() {
        return RouterFunctions.route()
                .GET("/router/customers", customerHandler::loadCustomers)
                .GET("/router/customers/stream", customerStreamHandler::loadCustomers)
                .GET("/router/customer/{input}", customerHandler::findCustomer)
                .POST("/router/customers/save", customerHandler::saveCustomer)
                .build();
    }

    @Bean
    // A Replacement of RestController and RequestMapping
    public RouterFunction<ServerResponse> productRouteFunction() {
        return RouterFunctions.route()
                .GET("/router/products/{id}", productHandler::getProductById)
                .build();
    }
}
