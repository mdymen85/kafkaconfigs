package com.retrysemantics.kafkaconfigs.normalconsumer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class Configs {

    @Value("${application.topic.from:normal-topic}")
    private String normalTopic;

    @Bean
    public NewTopic compactTopicExample() {
        return TopicBuilder.name(normalTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

}
