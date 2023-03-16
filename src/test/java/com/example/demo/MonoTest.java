package com.example.demo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

public class MonoTest {

    @Test
    public void testMono() {
        // Publisher
        Mono<String> name = Mono.just("Durgesh")
                // log each of the events published by the publisher
                .log();

        // Subscribe to the events published by the publisher
        name.subscribe(System.out::println);
    }

    @Test
    public void testMonoErrorEvents() {
        // Publisher
        Mono<?> name = Mono.just("Durgesh")
                // throwing exception
                .then(Mono.error(new RuntimeException("Exception occurred")))
                // log each of the events published by the publisher
                .log();

        // * Here onError() will be triggered
        // Subscribe to the events published by the publisher
        name.subscribe(
                System.out::println,
                e -> System.out.println("Error is " + e.getMessage()),
                null,
                s -> s.request(1)
        );
    }
}
