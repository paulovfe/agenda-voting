package com.paulovfe.agendavoting.entity;

import lombok.Data;

@Data
public class Vote {
    private final String cpf;
    private final Option option;
}
