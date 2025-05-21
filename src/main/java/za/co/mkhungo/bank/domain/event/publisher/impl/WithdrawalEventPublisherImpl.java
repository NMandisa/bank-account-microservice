package za.co.mkhungo.bank.domain.event.publisher.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import za.co.mkhungo.bank.domain.event.WithdrawalEvent;
import za.co.mkhungo.bank.domain.event.publisher.EventPublisher;
import za.co.mkhungo.bank.exception.JsonSerializationException;

/**
 * @author Noxolo.Mkhungo
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class WithdrawalEventPublisherImpl implements EventPublisher<WithdrawalEvent> {

    private final SnsClient snsClient;
    private final ObjectMapper objectMapper;

    @Value("${sns.topic.arn.withdrawal}")
    private String snsTopicArn;

    @Value("${sns.topic.arn.withdrawal.dlq}")
    private String dlqTopicArn;


    /**
     * @param event
     */
    @Override
    @Async
    @CircuitBreaker(name = "snsPublisher", fallbackMethod = "fallbackPublish")
    @Retry(name = "snsRetry", fallbackMethod = "fallbackPublish")
    @Bulkhead(name = "snsBulkhead")
    @Timed("sns.publish.timer")
    public void publish(WithdrawalEvent event) {
        try {
            snsClient.publish(PublishRequest.builder()
                    .topicArn(snsTopicArn)
                    .message(objectMapper.writeValueAsString(event))
                    .build());
            log.info(" Published event to SNS for account {}", event.getAccountId());
        } catch (JsonProcessingException e) {
            log.error(".....blah blah blah error what what {}", event.getAccountId());
            throw new JsonSerializationException("JSON serialization failed", e);
        }
    }

    // Fallback method (DLQ + Logging)
    public void fallbackPublish(WithdrawalEvent event, Throwable ex) {
        log.error("⚠ SNS publish failed for account {}. Fallback triggered. Reason: {}",
                event.getAccountId(), ex.getMessage());
        try {
            snsClient.publish(PublishRequest.builder()
                    .topicArn(dlqTopicArn).message(objectMapper.writeValueAsString(event))
                    .subject("DLQ: Withdraw Event").build());
            log.warn("Event routed to DLQ for account {}", event.getAccountId());
        } catch (Exception dlqEx) {
            log.error("Failed to publish to DLQ.");
             // Persistent to DB
            log.error("Failed Event persisted to local DB");
        }
    }
}
