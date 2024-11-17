package com.jobportal.joblistings.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.joblistings.service.IListingService;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ApplicationStatsConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationStatsConsumer.class);
    private final IListingService listingService;
    private final ObjectMapper objectMapper;

    public ApplicationStatsConsumer(IListingService listingService, ObjectMapper objectMapper) {
        this.listingService = listingService;
        this.objectMapper = objectMapper;
    }

    @Data
    static class ApplicationEvent {
        private String eventType;
        private Long jobListingId;
    }

    @KafkaListener(
            topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(ConsumerRecord<String, String> record) {
        try {
            logger.info("Received record: {}", record);
            String message = record.value();

            ApplicationEvent event = objectMapper.readValue(message, ApplicationEvent.class);

            if ("APPLICATION_CREATED".equals(event.getEventType())) {
                Long jobListingId = event.getJobListingId();
                logger.debug("Processing application created event for job listing: {}", jobListingId);
                listingService.incrementApplicationCount(jobListingId);
                logger.info("Successfully processed application created event for job listing: {}", jobListingId);
            }
        } catch (Exception e) {
            logger.error("Error processing application event. Message: {}", record.value(), e);
        }
    }
}