package com.paulovfe.agendavoting.repository;

import com.paulovfe.agendavoting.entity.Agenda;
import com.paulovfe.agendavoting.entity.AgendaStatus;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.stream.Stream;

public interface AgendaRepository extends MongoRepository<Agenda, ObjectId> {

    Stream<Agenda> findAllByStatusAndExpirationDateLessThan(AgendaStatus status, Instant expirationDate);

}
