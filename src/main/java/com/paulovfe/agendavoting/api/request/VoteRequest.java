package com.paulovfe.agendavoting.api.request;

import com.paulovfe.agendavoting.entity.Option;

import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class VoteRequest {
    @CPF
    private String cpf;
    @NotNull
    private Option option;
}
