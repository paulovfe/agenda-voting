package com.paulovfe.agendavoting;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AgendaVotingApplicationTests {

    @LocalServerPort
    private int port;

    @BeforeAll
    static void beforeAll() {
        final RestAssuredConfig config = RestAssuredConfig.config();
        config.getLogConfig().enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.config = config;
    }

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = this.port;
    }


}
