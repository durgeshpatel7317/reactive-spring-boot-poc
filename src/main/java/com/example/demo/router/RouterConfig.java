package com.example.demo.router;

import com.example.demo.handler.CustomerHandler;
import com.example.demo.handler.CustomerStreamHandler;
import com.example.demo.handler.FileHandler;
import com.example.demo.handler.KafkaHandler;
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
    private final KafkaHandler kafkaHandler;
    private final FileHandler fileHandler;

    @Autowired
    public RouterConfig(
        CustomerHandler customerHandler, CustomerStreamHandler customerStreamHandler,
        ProductHandler productHandler, KafkaHandler kafkaHandler, FileHandler fileHandler
    ) {
        this.customerHandler = customerHandler;
        this.customerStreamHandler = customerStreamHandler;
        this.productHandler = productHandler;
        this.kafkaHandler = kafkaHandler;
        this.fileHandler = fileHandler;
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


    @Bean
    public RouterFunction<ServerResponse> fileRouterFunction() {
        return RouterFunctions.route()
            .GET("/router/file/posts", fileHandler::getDataFromJsonFile)
            .POST("/router/file/posts", fileHandler::writeDateToFile)
            .POST("/router/publish/kafka", kafkaHandler::publishMessageToKafkaTopic)
            .build();
    }
}
