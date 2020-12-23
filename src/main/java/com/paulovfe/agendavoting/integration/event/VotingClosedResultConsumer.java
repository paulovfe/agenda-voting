package com.paulovfe.agendavoting.integration.event;

import com.paulovfe.agendavoting.dto.ResultDTO;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VotingClosedResultConsumer {

    @KafkaListener(topics = "${voting.closed.result.topic}")
    public void consume(final ResultDTO message) {
        log.info("Consuming result {}", message);
    }

}
