package com.retrysemantics.kafkaconfigs.retryconsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetryWithTopicConsumer {

    private static int TIME_SECONDS = 0;
    private static int DESIRED_SECONDS = 2;

    @Value("${application.topic.retry-consumer:retry-consumer-topic}")
    private String retryTopic;

    @Value("${application.topic.reply:reply-topic}")
    private String replyTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Will receive an event and will retry 3 times with a delay of 1000ms
     * @param eventConsumer
     * @throws Exception
     */
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 3.0))
    @KafkaListener(topics = "${application.topic.retry-consumer}", groupId = "listener")
    public void consumer(String eventConsumer) throws Exception {
        log.info("Reading message {} from topic {}.", eventConsumer, retryTopic);

        log.info("Time elapsed {}", TIME_SECONDS);

        Thread.sleep(DESIRED_SECONDS * 1000L);
        TIME_SECONDS = TIME_SECONDS + DESIRED_SECONDS;

        throw new Exception();

    }

    /**
     * Will consume from the first retry topic
     * @param eventConsumer
     * @throws Exception
     */
    @KafkaListener(topics = "retry-consumer-topic-retry-1000", groupId = "listener")
    public void consumerRetry(String eventConsumer) throws Exception {
        try {
            log.info("Retry Topic ->> Reading message {} from topic {}.", eventConsumer, retryTopic);

            log.info("Retry Topic ->> Time elapsed {}", TIME_SECONDS);

            Thread.sleep(DESIRED_SECONDS * 1000L);
            TIME_SECONDS = TIME_SECONDS + DESIRED_SECONDS;

            throw new Exception();
        }
        catch (Exception e) {
            kafkaTemplate.send(replyTopic, "error in " + eventConsumer);
        }

    }

}
