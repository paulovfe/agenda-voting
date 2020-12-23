package com.paulovfe.agendavoting.component;

import com.github.tomakehurst.wiremock.common.Json;
import com.paulovfe.agendavoting.integration.httpclient.reponse.CPFStatus;
import com.paulovfe.agendavoting.integration.httpclient.reponse.CPFStatusResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class MockUserInfoDetail {

    public static void mockUserAble(final String cpf) {
        mock(cpf, CPFStatus.ABLE_TO_VOTE);
    }

    public static void mockUserUnable(final String cpf) {
        mock(cpf, CPFStatus.UNABLE_TO_VOTE);
    }

    private static void mock(final String cpf, final CPFStatus status) {
        final CPFStatusResponse response = new CPFStatusResponse(status);
        stubFor(get(urlEqualTo("/user-info/users/" + cpf))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(200)
                                .withBody(Json.write(response))));
    }


}
