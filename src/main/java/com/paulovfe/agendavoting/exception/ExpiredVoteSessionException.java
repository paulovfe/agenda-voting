package com.paulovfe.agendavoting.exception;

public class ExpiredVoteSessionException extends VoteException {
    public ExpiredVoteSessionException() {
        super("Agenda is expired.");
    }
}
