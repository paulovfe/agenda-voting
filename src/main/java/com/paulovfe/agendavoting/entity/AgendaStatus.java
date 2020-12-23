package com.paulovfe.agendavoting.entity;

import com.paulovfe.agendavoting.exception.InvalidStatusChangeException;

import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AgendaStatus {
    CLOSED(null),
    OPENED(CLOSED),
    CREATED(OPENED);

    private final AgendaStatus next;

    public Optional<AgendaStatus> getNext() {
        return Optional.ofNullable(next);
    }

    public static void validChange(final AgendaStatus current, final AgendaStatus future) {
        if (!future.equals(current.getNext().orElse(null))) {
            throw new InvalidStatusChangeException(current);
        }
    }
}
