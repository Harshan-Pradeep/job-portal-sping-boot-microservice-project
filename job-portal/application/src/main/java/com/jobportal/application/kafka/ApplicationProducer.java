package com.jobportal.application.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationProducer.class);

    private final NewTopic applicationTopic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ApplicationProducer(NewTopic applicationTopic, KafkaTemplate<String, String> kafkaTemplate) {
        this.applicationTopic = applicationTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageWithKey(String key, String message) {
        LOGGER.info("Sending message with key {} to topic {}: {}", key, applicationTopic.name(), message);

        Message<String> msg = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, applicationTopic.name())
                .setHeader(KafkaHeaders.KEY, key)
                .build();

        kafkaTemplate.send(msg);
    }
}