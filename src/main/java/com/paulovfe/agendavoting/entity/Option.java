package com.paulovfe.agendavoting.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Option {

    NO("Não"),
    YES("Sim");

    private final String option;

}
