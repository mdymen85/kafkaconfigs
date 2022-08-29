package com.retrysemantics.kafkaconfigs.retryconsumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class RetryConsumerConfig {

    @Value("${application.topic.retry-consumer:retry-topic}")
    private String retryTopic;

    @Value("${application.topic.reply:reply-topic}")
    private String replyTopic;

    @Bean
    public NewTopic retryConsumerTopic() {
        return TopicBuilder.name(retryTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic replyConsumerTopic() {
        return TopicBuilder.name(replyTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

}
