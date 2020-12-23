package com.paulovfe.agendavoting.service;

import com.paulovfe.agendavoting.dto.AgendaDTO;
import com.paulovfe.agendavoting.entity.Agenda;
import com.paulovfe.agendavoting.entity.AgendaStatus;
import com.paulovfe.agendavoting.exception.NotFoundException;
import com.paulovfe.agendavoting.integration.event.VotingClosedResultProducer;
import com.paulovfe.agendavoting.mapper.AgendaMapper;
import com.paulovfe.agendavoting.repository.AgendaRepository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.paulovfe.agendavoting.entity.AgendaStatus.CLOSED;
import static com.paulovfe.agendavoting.entity.AgendaStatus.CREATED;
import static com.paulovfe.agendavoting.entity.AgendaStatus.OPENED;


@Slf4j
@RequiredArgsConstructor
@Service
public class AgendaService {

    private final AgendaRepository repository;
    private final AgendaMapper mapper;
    private final VotingClosedResultProducer producer;

    @Value("${default.expiration.session:1}")
    private Integer defaultExpiration;

    public AgendaDTO create(final AgendaDTO agenda) {
        log.debug("Creating new agenda");
        final Agenda entity = mapper.toEntity(agenda);
        entity.setStatus(CREATED);
        return mapper.toDTO(repository.save(entity));
    }

    public AgendaDTO get(final String id) {
        log.debug("Get agenda {}", id);
        return repository.findById(new ObjectId(id))
                .map(mapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Agenda not found!"));
    }

    public List<AgendaDTO> listAll() {
        log.debug("Listing all agendas");
        final Sort dateSort = Sort.sort(Agenda.class).by(Agenda::getCreatedDate).descending();
        return repository.findAll(dateSort).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public AgendaDTO doOpenVote(final String id, final Integer expiration) {
        log.debug("Open voting agenda {}", id);
        return repository.findById(new ObjectId(id))
                .map(agenda -> calculateExpirationDate(agenda, expiration))
                .map(agenda -> changeStatus(agenda, OPENED))
                .map(mapper::toDTO)
                .orElseThrow(() -> new NotFoundException("Agenda not found!"));
    }

    public void closeExpiredAgenda() {
        log.debug("Closing expired agenda");
        repository.findAllByStatusAndExpirationDateLessThan(OPENED, Instant.now())
                .map(this::closeAgenda)
                .map(mapper::toResult)
                .forEach(producer::send);
    }

    private Agenda closeAgenda(final Agenda agenda) {
        log.debug("Closing agenda {}", agenda.getId());
        return changeStatus(agenda, CLOSED);
    }

    private Agenda calculateExpirationDate(final Agenda agenda, final Integer expiration) {
        final Integer expirationToUse = Optional.ofNullable(expiration).orElse(defaultExpiration);
        agenda.setExpirationDate(Instant.now().plus(expirationToUse, ChronoUnit.MINUTES));
        return agenda;
    }

    private Agenda changeStatus(final Agenda agenda, final AgendaStatus status) {
        log.debug("Changing status agenda [{}] statusOld [{}] to statusNew [{}]", agenda.getId(), agenda.getStatus(), status);
        return Optional.of(agenda)
                .filter(ag -> verifyChangeStatus(ag, status))
                .map(ag -> {
                    agenda.setStatus(status);
                    return agenda;
                })
                .map(repository::save)
                .orElseThrow(() -> new NotFoundException("Agenda not found!"));
    }

    private boolean verifyChangeStatus(final Agenda agenda, final AgendaStatus futureStatus) {
        AgendaStatus.validChange(agenda.getStatus(), futureStatus);
        return true;
    }

}
