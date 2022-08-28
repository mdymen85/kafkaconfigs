package com.retrysemantics.kafkaconfigs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@Slf4j
public class ProducerController {

    @Value("${application.topic.to:normal-topic}")
    private String topic;

    @Value("${application.retry.topic.to:retry-topic}")
    private String retryTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping(path = "/v1/normal", method = RequestMethod.POST)
    public ResponseEntity<EventProducer> post(@RequestBody EventProducer eventProducer) {

        log.info("Sending message {} to topic {}.", eventProducer, topic);

        kafkaTemplate.send(topic, eventProducer.getData());

        return new ResponseEntity<EventProducer>(eventProducer, HttpStatus.ACCEPTED);

    }

    @RequestMapping(path = "/v1/retry", method = RequestMethod.POST)
    public ResponseEntity<EventProducer> postRetry(@RequestBody EventProducer eventProducer) {

        log.info("Sending message {} to topic {}.", eventProducer, retryTopic);

        kafkaTemplate.send(retryTopic, eventProducer.getData());

        return new ResponseEntity<EventProducer>(eventProducer, HttpStatus.ACCEPTED);

    }
}
