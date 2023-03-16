package com.example.demo.handler;

import com.example.demo.dto.ProductDto;
import com.example.demo.exceptions.ProductNotFound;
import com.example.demo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ProductHandler {
    private final ProductService productService;

    @Autowired
    public ProductHandler(ProductService productService) {
        this.productService = productService;
    }

    public Mono<ServerResponse> getProductById(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<ProductDto> productDtoMono = productService.getProduct(id)
            .doOnNext(e -> log.info("Found product is {}", e))
            .switchIfEmpty(Mono.error(new ProductNotFound("Product not found with id " + id)));
        return ServerResponse.ok().body(productDtoMono, ProductDto.class);
    }
}