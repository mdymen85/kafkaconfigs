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
public class ReplyConsumer {

    private static int TIME_SECONDS = 0;
    private static int DESIRED_SECONDS = 2;

    @Value("${application.topic.reply:reply-topic}")
    private String replyTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * When happend an exception in the {@link RetryWithTopicConsumer} class, in the catch block
     * will send a message to a reply queue.
     * @param eventConsumer
     * @throws Exception
     */
    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 3.0))
    @KafkaListener(topics = "${application.topic.reply}", groupId = "listener")
    public void consumer(String eventConsumer) throws Exception {
        log.info("Reading message {} from topic {}.", eventConsumer, replyTopic);

        log.info("Time elapsed {}", TIME_SECONDS);

        Thread.sleep(DESIRED_SECONDS * 1000L);
        TIME_SECONDS = TIME_SECONDS + DESIRED_SECONDS;

        throw new Exception();

    }
}
