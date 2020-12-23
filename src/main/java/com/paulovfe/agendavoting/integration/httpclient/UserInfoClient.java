package com.paulovfe.agendavoting.integration.httpclient;


import com.paulovfe.agendavoting.integration.httpclient.reponse.CPFStatusResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "UserInfoClient", url = "${user-info.url}")
public interface UserInfoClient {

    @GetMapping("/users/{cpf}")
    Optional<CPFStatusResponse> getCpfStatus(@PathVariable String cpf);

}
