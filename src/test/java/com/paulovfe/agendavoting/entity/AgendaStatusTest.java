package com.paulovfe.agendavoting.entity;

import com.paulovfe.agendavoting.exception.InvalidStatusChangeException;

import org.junit.jupiter.api.Test;

import static com.paulovfe.agendavoting.entity.AgendaStatus.CLOSED;
import static com.paulovfe.agendavoting.entity.AgendaStatus.CREATED;
import static com.paulovfe.agendavoting.entity.AgendaStatus.OPENED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AgendaStatusTest {

    @Test
    void validStatusChange() {
        assertDoesNotThrow(() -> {
            AgendaStatus.validChange(CREATED, OPENED);
            AgendaStatus.validChange(OPENED, CLOSED);
        });
    }

    @Test
    void validStatusChangeInvalid() {
        assertThrows(InvalidStatusChangeException.class, () -> AgendaStatus.validChange(CREATED, CLOSED));
        assertThrows(InvalidStatusChangeException.class, () -> AgendaStatus.validChange(CLOSED, OPENED));
        assertThrows(InvalidStatusChangeException.class, () -> AgendaStatus.validChange(OPENED, CREATED));
    }
}