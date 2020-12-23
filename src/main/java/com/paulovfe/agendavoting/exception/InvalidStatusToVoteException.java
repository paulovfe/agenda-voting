package com.paulovfe.agendavoting.exception;

public class InvalidStatusToVoteException extends VoteException {
    public InvalidStatusToVoteException() {
        super("Agenda status is not OPEN.");
    }
}
