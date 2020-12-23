package com.paulovfe.agendavoting.exception;

public class CPFUnableToVoteException extends VoteException {
    public CPFUnableToVoteException() {
        super("CPF unable to vote.");
    }
}
