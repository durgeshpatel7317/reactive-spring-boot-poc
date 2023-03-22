package com.example.demo.handler;

import com.example.demo.dto.PostDto;
import com.example.demo.kafka.producer.KafkaProducer;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
public class KafkaHandler {
    private final KafkaProducer kafkaProducer;

    @Autowired
    public KafkaHandler(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public Mono<ServerResponse> publishMessageToKafkaTopic(ServerRequest request) {
        log.info("Received the request for publishing the message");
        Gson gson = new Gson();
        Flux<String> postDtoFluxString = request.bodyToFlux(PostDto.class)
            .map(gson::toJson);

        return
            kafkaProducer.sendMessage("posts", postDtoFluxString)
                .then(ServerResponse.accepted()
                    .body(Mono.just(Map.of("message", "Data will be published to kafka...!")), Map.class)
                );
    }
}
