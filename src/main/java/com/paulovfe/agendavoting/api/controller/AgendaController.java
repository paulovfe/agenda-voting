package com.paulovfe.agendavoting.api.controller;

import com.paulovfe.agendavoting.api.model.AgendaModel;
import com.paulovfe.agendavoting.api.model.ApiError;
import com.paulovfe.agendavoting.api.request.AgendaRequest;
import com.paulovfe.agendavoting.api.request.OpenSessionRequest;
import com.paulovfe.agendavoting.api.request.VoteRequest;
import com.paulovfe.agendavoting.dto.AgendaDTO;
import com.paulovfe.agendavoting.dto.ResultDTO;
import com.paulovfe.agendavoting.mapper.AgendaMapper;
import com.paulovfe.agendavoting.mapper.VoteMapper;
import com.paulovfe.agendavoting.service.AgendaService;
import com.paulovfe.agendavoting.service.VoteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "api/v1/agenda", produces = "application/json")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;
    private final VoteService voteService;
    private final AgendaMapper agendaMapper;
    private final VoteMapper voteMapper;

    @Operation(summary = "Create a new agenda")
    @ApiResponse(responseCode = "200", description = "Agenda created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AgendaModel.class))})
    @ApiResponse(responseCode = "400", description = "Invalid name", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @PostMapping
    public ResponseEntity<AgendaModel> create(
            @RequestBody @Valid final AgendaRequest agendaRequest
    ) {
        final AgendaDTO agendaDTO = agendaService.create(agendaMapper.toDTO(agendaRequest));
        final AgendaModel agenda = agendaMapper.toModel(agendaDTO);
        return new ResponseEntity<>(agenda, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a agenda by its id")
    @ApiResponse(responseCode = "200", description = "Found agenda", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AgendaModel.class))})
    @ApiResponse(responseCode = "404", description = "Agenda not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @ApiResponse(responseCode = "400", description = "Invalid is supplied", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @GetMapping("/{id}")
    public ResponseEntity<AgendaModel> get(
            @Parameter(description = "id of agenda to be searched") @PathVariable final String id
    ) {
        final AgendaDTO agendaDTO = agendaService.get(id);
        final AgendaModel model = agendaMapper.toModel(agendaDTO);
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Get all agendas")
    @ApiResponse(responseCode = "200", description = "List of all agenda", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AgendaModel[].class))})
    @GetMapping
    public ResponseEntity<List<AgendaModel>> getAll() {
        final List<AgendaModel> model = this.agendaService.listAll().stream()
                .map(agendaMapper::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Open an agenda to voting")
    @ApiResponse(responseCode = "200", description = "Agenda opened to voting", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AgendaModel.class))})
    @ApiResponse(responseCode = "404", description = "Agenda not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @ApiResponse(responseCode = "400", description = "Status invalid to open", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @PutMapping("/{id}/open-vote")
    public ResponseEntity<AgendaModel> openVote(
            @Parameter(description = "id of agenda to open vote") @PathVariable final String id,
            @RequestBody @Valid final OpenSessionRequest request
    ) {
        final AgendaDTO agendaDTO = agendaService.doOpenVote(id, request.getMinutesToExpiration());
        final AgendaModel model = agendaMapper.toModel(agendaDTO);
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Vote in an agenda")
    @ApiResponse(responseCode = "204", description = "Agenda voted")
    @ApiResponse(responseCode = "404", description = "Agenda not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @ApiResponse(responseCode = "400", description = "Already voted", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @PutMapping("/{id}/vote")
    public ResponseEntity<Void> vote(
            @Parameter(description = "id of agenda to vote") @PathVariable final String id,
            @RequestBody @Valid final VoteRequest request
    ) {
        voteService.doVote(id, voteMapper.toDTO(request));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Votes result")
    @ApiResponse(responseCode = "200", description = "Result of agenda votes", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResultDTO.class))})
    @ApiResponse(responseCode = "404", description = "Agenda not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))})
    @GetMapping("/{id}/voting-result")
    public ResponseEntity<ResultDTO> result(
            @Parameter(description = "id of agenda to view the result") @PathVariable final String id
    ) {
        final ResultDTO result = voteService.getResult(id);
        return ResponseEntity.ok(result);
    }
}