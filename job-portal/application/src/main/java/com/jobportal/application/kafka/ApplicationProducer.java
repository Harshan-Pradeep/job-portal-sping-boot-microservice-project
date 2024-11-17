package com.jobportal.application.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
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
    private final ObjectMapper objectMapper;

    public ApplicationProducer(NewTopic applicationTopic, KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.applicationTopic = applicationTopic;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Data
    public static class ApplicationEvent {
        private String eventType;
        private Long jobListingId;

        public ApplicationEvent(String eventType, Long jobListingId) {
            this.eventType = eventType;
            this.jobListingId = jobListingId;
        }
    }

    public void sendApplicationCreatedEvent(Long jobListingId) {
        try {
            ApplicationEvent event = new ApplicationEvent("APPLICATION_CREATED", jobListingId);
            String message = objectMapper.writeValueAsString(event);
            String key = jobListingId.toString();

            LOGGER.info("Sending application created event for job listing {}: {}", jobListingId, message);

            Message<String> msg = MessageBuilder
                    .withPayload(message)
                    .setHeader(KafkaHeaders.TOPIC, applicationTopic.name())
                    .setHeader(KafkaHeaders.KEY, key)
                    .build();

            kafkaTemplate.send(msg);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error serializing application event", e);
            throw new RuntimeException("Error sending application event", e);
        }
    }
}