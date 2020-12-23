package com.paulovfe.agendavoting.exception;

public class InvalidStatusToResultException extends VoteException {
    public InvalidStatusToResultException() {
        super("Agenda status is not CLOSED.");
    }
}
