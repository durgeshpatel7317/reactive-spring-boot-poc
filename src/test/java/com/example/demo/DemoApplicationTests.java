package com.example.demo;

import com.example.demo.controller.ProductController;
import com.example.demo.dto.ProductDto;
import com.example.demo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

@Slf4j
@WebFluxTest(ProductController.class)
class DemoApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    String id1 = UUID.randomUUID().toString();
    String id2 = UUID.randomUUID().toString();

    @Test
    public void addProductTest() {
        Mono<ProductDto> dtoMono = Mono.just(
            ProductDto.builder()
                .id(UUID.randomUUID().toString())
                .qty(2)
                .price(15000.0)
                .name("Shoes")
                .build()
        );
        // Mocking the service layer
        Mockito.when(productService.saveProduct(dtoMono)).thenReturn(dtoMono);
        // Testing the APIs
        Flux<ProductDto> productDtoFlux = webTestClient.post().uri("/products/")
            .body(dtoMono, ProductDto.class)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .returnResult(ProductDto.class)
            .getResponseBody();

        productDtoFlux.subscribe(e -> System.out.println("Value of return is " + e));
    }

    @Test
    public void getProductTest() {
        Flux<ProductDto> dtoFlux = Flux.just(
            ProductDto.builder()
                .id(id1)
                .qty(2)
                .price(15000.0)
                .name("Shoes")
                .build(),
            ProductDto.builder()
                .id(id2)
                .qty(2)
                .price(15000.0)
                .name("Laptops")
                .build()
        );
        // Mocking the service layer
        Mockito.when(productService.getProducts()).thenReturn(dtoFlux);
        // Testing the APIs
        Flux<ProductDto> response = webTestClient.get().uri("/products/")
            .exchange()
            .expectStatus().is2xxSuccessful()
            .returnResult(ProductDto.class)
            .getResponseBody();

        StepVerifier.create(response)
            .expectSubscription()
            // Used to verify the object returned from DB
            .expectNext(ProductDto.builder()
                .id(id1)
                .qty(2)
                .price(15000.0)
                .name("Shoes")
                .build()
            )
            .expectNext(ProductDto.builder()
                .id(id2)
                .qty(2)
                .price(15000.0)
                .name("Laptops")
                .build()
            )
            .verifyComplete();
    }
}
