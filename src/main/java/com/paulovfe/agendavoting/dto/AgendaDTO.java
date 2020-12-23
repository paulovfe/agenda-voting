package com.paulovfe.agendavoting.dto;

import com.paulovfe.agendavoting.entity.AgendaStatus;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class AgendaDTO {
    private final String id;
    private final String name;
    private final AgendaStatus status;
    private final Instant createdDate;
    private Instant expirationDate;
}
