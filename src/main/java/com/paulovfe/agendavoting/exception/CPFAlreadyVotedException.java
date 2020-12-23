package com.paulovfe.agendavoting.exception;

public class CPFAlreadyVotedException extends VoteException {
    public CPFAlreadyVotedException() {
        super("CPF already voted.");
    }
}
