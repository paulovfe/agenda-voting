package com.paulovfe.agendavoting.integration.event;

import com.paulovfe.agendavoting.dto.ResultDTO;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class VotingClosedResultProducer {

    @Value("${voting.closed.result.topic}")
    private String kafkaTopic;

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public void send(final ResultDTO result) {
        log.info("Publishing result {}", result);
        kafkaTemplate.send(new ProducerRecord<>(kafkaTopic, generateKey(), result));
    }

    private String generateKey() {
        return RandomStringUtils.random(10);
    }
}
