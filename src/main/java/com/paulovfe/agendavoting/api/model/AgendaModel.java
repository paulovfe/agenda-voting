package com.paulovfe.agendavoting.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.paulovfe.agendavoting.entity.AgendaStatus;

import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class AgendaModel extends RepresentationModel<AgendaModel> {
    private final String id;
    private final String name;
    private final Instant createdDate;
    private final AgendaStatus status;
    private Instant expirationDate;
}
