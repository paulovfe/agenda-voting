package com.paulovfe.agendavoting.exception;

import com.paulovfe.agendavoting.entity.AgendaStatus;

public class InvalidStatusChangeException extends RuntimeException {

    private final AgendaStatus current;

    public InvalidStatusChangeException(final AgendaStatus current) {
        this.current = current;
    }

    @Override
    public String getMessage() {
        return createMessage();
    }

    private String createMessage() {
        return "Can't change status from Agenda. " +
                "Current status is " + current + "." +
                current.getNext()
                        .map(next -> " Only possible change is " + next + ".")
                        .orElse("");
    }
}
