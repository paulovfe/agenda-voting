package com.paulovfe.agendavoting.dto;

import com.paulovfe.agendavoting.entity.Option;

import lombok.Data;

@Data
public class VoteDTO {
    private String cpf;
    private Option option;
}
