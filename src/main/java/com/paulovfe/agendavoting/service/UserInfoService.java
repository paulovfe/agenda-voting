package com.paulovfe.agendavoting.service;

import com.paulovfe.agendavoting.integration.httpclient.UserInfoClient;
import com.paulovfe.agendavoting.integration.httpclient.reponse.CPFStatus;
import com.paulovfe.agendavoting.integration.httpclient.reponse.CPFStatusResponse;

import org.springframework.stereotype.Service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoService {
    private final UserInfoClient userInfoClient;

    public Optional<CPFStatus> getCpfStatus(final String cpf) {
        return userInfoClient.getCpfStatus(cpf)
                .map(CPFStatusResponse::getStatus);
    }
}
