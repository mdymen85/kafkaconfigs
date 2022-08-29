package com.retrysemantics.kafkaconfigs.normalconsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Consumer {

    private static int TIME_SECONDS = 0;
    private static int DESIRED_SECONDS = 2;

    @Value("${application.topic.consumer:normal-topic}")
    private String normalTopic;

    /**
     * Will receive an event and will retry 10 times and, until
     * the first message won't end retrying, the second message won't start
     * processing.
     * @param eventConsumer
     * @throws Exception
     */
    @KafkaListener(topics = "${application.topic.consumer}", groupId = "listener")
    public void consumer(String eventConsumer) throws Exception {
        log.info("Reading message {} from topic {}.", eventConsumer, normalTopic);

        log.info("Time elapsed {}", TIME_SECONDS);

        Thread.sleep(DESIRED_SECONDS * 1000L);
        TIME_SECONDS = TIME_SECONDS + DESIRED_SECONDS;

        throw new Exception();

    }

}
