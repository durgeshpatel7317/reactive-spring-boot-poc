package com.example.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxTest {
    @Test
    public void fluxTest() {
        Flux<String> name = Flux.just("Durgesh", "Patel")
            .concatWith(Mono.just("Bangalore"))
            .log();

        name.subscribe(System.out::println);
    }

    @Test
    public void fluxExceptionTest() {
        Flux<String> name = Flux.just("Durgesh", "Patel")
            // Here onNext() will only be called twice followed by onError
            .concatWith(Mono.error(new RuntimeException("Exception occurred")))
            // onNext will not be called for Bangalore
            .concatWith(Mono.just("Bangalore"))
            .log();

        name.subscribe(System.out::println, e -> System.out.println(e.getMessage()));
    }
}
