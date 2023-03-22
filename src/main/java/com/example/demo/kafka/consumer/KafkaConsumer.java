package com.example.demo.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;

@Slf4j
@Component
public class KafkaConsumer {
    private final ReceiverOptions<String, String> receiverOptions;

    public KafkaConsumer(ReceiverOptions<String, String> receiverOptions) {
        this.receiverOptions = receiverOptions;
    }

    @EventListener(ApplicationStartedEvent.class)
    public void consumeMessages() {
        ReceiverOptions<String, String> options = receiverOptions
            .subscription(Collections.singletonList("posts"))
            .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
            .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));

        KafkaReceiver.create(options)
            .receive()
            .subscribe(record -> {
                ReceiverOffset offset = record.receiverOffset();
                log.debug("Received message: topic-partition={} offset={} key={} value={}\n",
                    offset.topicPartition(),
                    offset.offset(),
                    record.key(),
                    record.value());
                offset.acknowledge();
            });
    }
}
