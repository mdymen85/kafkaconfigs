package com.retrysemantics.kafkaconfigs.retry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetryConsumer {

    private static int TIME_SECONDS = 0;
    private static int DESIRED_SECONDS = 2;

    @Value("${application.topic.retry:retry-topic}")
    private String retryTopic;

    /**
     * Will receive an event and will retry 3 times with a backoff of
     * 1000 miliseconds, passing throw the listeners <b>listener-retry-backoffnumber</b>
     * mixing with others messages
     * @param eventConsumer
     * @throws Exception
     */
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 3.0))
    @KafkaListener(topics = "${application.topic.retry}", groupId = "listener")
    public void consumer(String eventConsumer) throws Exception {
        log.info("Reading message {} from topic {}.", eventConsumer, retryTopic);

        log.info("Time elapsed {}", TIME_SECONDS);

        Thread.sleep(DESIRED_SECONDS * 1000L);
        TIME_SECONDS = TIME_SECONDS + DESIRED_SECONDS;

        throw new Exception();

    }

}
