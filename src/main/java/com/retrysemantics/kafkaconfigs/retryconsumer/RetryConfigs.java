package com.retrysemantics.kafkaconfigs.retryconsumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class RetryConfigs {

    @Value("${application.retry.topic.from:retry-topic}")
    private String retryTopic;

    @Bean
    public NewTopic retryTopic() {
        return TopicBuilder.name(retryTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

}
