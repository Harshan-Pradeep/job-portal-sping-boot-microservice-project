package com.jobportal.users.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ApplicationStatsConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStatsConsumer.class);

    @KafkaListener(
            topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )

    public void consume(final ConsumerRecord<String, String> record) {
        LOGGER.info("Received record: {}", record);
    }
}
