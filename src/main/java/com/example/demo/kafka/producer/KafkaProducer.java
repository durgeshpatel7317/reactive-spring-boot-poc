package com.example.demo.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class KafkaProducer {
    private final KafkaSender<String, String> sender;

    @Autowired
    public KafkaProducer(KafkaSender<String, String> sender) {
        this.sender = sender;
    }

    public Flux<SenderResult<String>> sendMessage(String topic, Flux<String> message) {
        AtomicInteger atmInt = new AtomicInteger();
        return sender.send(
                message.map(m -> SenderRecord.create(new ProducerRecord<>(topic, "Message_" + atmInt.incrementAndGet(), m), m))
            )
            .doOnError(err -> log.error("Error while publishing the message {}", err.getMessage()))
            .doOnNext(event -> {
                RecordMetadata metadata = event.recordMetadata();
                log.debug("Message {} sent successfully, topic-partition={}-{} offset={}\n",
                    event.correlationMetadata(),
                    metadata.topic(),
                    metadata.partition(),
                    metadata.offset());
            })
            .doOnComplete(() -> log.info("Publishing message to kafka is success"));
    }
}
