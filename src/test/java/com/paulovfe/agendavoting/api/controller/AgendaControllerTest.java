package com.paulovfe.agendavoting.api.controller;

import com.paulovfe.agendavoting.AgendaVotingApplicationTests;
import com.paulovfe.agendavoting.api.request.AgendaRequest;
import com.paulovfe.agendavoting.api.request.OpenSessionRequest;
import com.paulovfe.agendavoting.api.request.VoteRequest;
import com.paulovfe.agendavoting.component.AgendaCreatorComponent;
import com.paulovfe.agendavoting.component.CPFHelper;
import com.paulovfe.agendavoting.component.MockUserInfoDetail;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.stream.IntStream;

import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;

import static com.paulovfe.agendavoting.entity.Option.YES;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AgendaControllerTest extends AgendaVotingApplicationTests {

    private static final String AGENDA_MAPPING = "/api/v1/agenda/";

    private final AgendaCreatorComponent agendaCreatorComponent;

    @Test
    void createSuccessful() {
        final AgendaRequest request = new AgendaRequest("Agenda test");
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .post(AGENDA_MAPPING)
                .then()
                .log().all(true)
                .statusCode(HttpStatus.CREATED.value())
                .body(notNullValue())
                .body("id", is(notNullValue()));
    }

    @Test
    void getSuccessful() {
        final String agendaId = agendaCreatorComponent.create().getId();

        given()
                .contentType(ContentType.JSON)
                .get(AGENDA_MAPPING + agendaId)
                .then()
                .log().all(true)
                .statusCode(HttpStatus.OK.value())
                .body(notNullValue())
                .body("id", equalTo(agendaId));
    }

    @Test
    void getAllSuccessful() {
        IntStream.range(0, 10).forEach(i -> agendaCreatorComponent.create());
        given()
                .contentType(ContentType.JSON)
                .get(AGENDA_MAPPING)
                .then()
                .log().all(true)
                .statusCode(HttpStatus.OK.value())
                .body(notNullValue())
                .body("size()", is(agendaCreatorComponent.currentCount().intValue()));
    }

    @Test
    void openVoteSuccessful() {
        final String agendaId = agendaCreatorComponent.create().getId();

        final OpenSessionRequest request = new OpenSessionRequest();
        request.setMinutesToExpiration(10);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .put(AGENDA_MAPPING + agendaId + "/open-vote")
                .then()
                .log().all(true)
                .statusCode(HttpStatus.OK.value())
                .body(notNullValue())
                .body("id", equalTo(agendaId));
    }

    @Test
    void voteUserAbleSuccessful() {
        final String agendaId = agendaCreatorComponent.createOpened().getId();

        final VoteRequest voteRequest = new VoteRequest();
        voteRequest.setCpf(CPFHelper.generate());
        voteRequest.setOption(YES);

        MockUserInfoDetail.mockUserAble(voteRequest.getCpf());

        given()
                .contentType(ContentType.JSON)
                .body(voteRequest)
                .put(AGENDA_MAPPING + agendaId + "/vote")
                .then()
                .log().all(true)
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void voteUserUnableError() {
        final String agendaId = agendaCreatorComponent.createOpened().getId();

        final VoteRequest voteRequest = new VoteRequest();
        voteRequest.setCpf(CPFHelper.generate());
        voteRequest.setOption(YES);

        MockUserInfoDetail.mockUserUnable(voteRequest.getCpf());

        given()
                .contentType(ContentType.JSON)
                .body(voteRequest)
                .put(AGENDA_MAPPING + agendaId + "/vote")
                .then()
                .log().all(true)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo("BAD_REQUEST"))
                .body("message", equalTo("CPF unable to vote."));
    }

    @Test
    void resultSuccessful() {
        final int votesYes = RandomUtils.nextInt(1, 50);
        final int votesNo = RandomUtils.nextInt(1, 50);
        final String agendaId = agendaCreatorComponent.createOpenedAndVote(votesYes, votesNo).getId();

        given()
                .contentType(ContentType.JSON)
                .get(AGENDA_MAPPING + agendaId + "/voting-result")
                .then()
                .log().all(true)
                .statusCode(HttpStatus.OK.value())
                .body("result.YES", equalTo(votesYes))
                .body("result.NO", equalTo(votesNo));
    }
}