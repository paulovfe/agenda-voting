package com.paulovfe.agendavoting.mapper;

import com.paulovfe.agendavoting.api.controller.AgendaController;
import com.paulovfe.agendavoting.api.model.AgendaModel;
import com.paulovfe.agendavoting.api.request.AgendaRequest;
import com.paulovfe.agendavoting.api.request.OpenSessionRequest;
import com.paulovfe.agendavoting.api.request.VoteRequest;
import com.paulovfe.agendavoting.dto.AgendaDTO;
import com.paulovfe.agendavoting.dto.ResultDTO;
import com.paulovfe.agendavoting.entity.Agenda;
import com.paulovfe.agendavoting.entity.AgendaStatus;
import com.paulovfe.agendavoting.entity.Option;
import com.paulovfe.agendavoting.entity.Vote;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Mapper
public abstract class AgendaMapper {
    @Mapping(target = "id", expression = "java(agenda.getId() == null ? null : new org.bson.types.ObjectId(agenda.getId()))")
    @Mapping(target = "votes", ignore = true)
    public abstract Agenda toEntity(AgendaDTO agenda);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expirationDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    public abstract AgendaDTO toDTO(AgendaRequest agenda);

    @Mapping(target = "id", expression = "java(agenda.getId().toString())")
    public abstract AgendaDTO toDTO(Agenda agenda);

    public abstract AgendaModel toModel(AgendaDTO agendaDTO);

    public ResultDTO toResult(final Agenda agenda) {
        final List<Vote> votes = agenda.getVotes();
        final ResultDTO result = new ResultDTO();
        for (final Option option : Option.values()) {
            final long count = votes.stream().filter(vote -> vote.getOption().equals(option)).count();
            result.getResult().put(option, count);
        }
        return result;
    }

    @AfterMapping
    protected void fillLink(@MappingTarget final AgendaModel agendaModel, final AgendaDTO agendaDTO) {
        final AgendaController agendaController = methodOn(AgendaController.class);
        agendaModel.add(linkTo(agendaController.get(agendaDTO.getId())).withSelfRel());
        if (AgendaStatus.CREATED.equals(agendaModel.getStatus())) {
            agendaModel.add(linkTo(agendaController.openVote(agendaDTO.getId(), new OpenSessionRequest())).withRel("open-session"));
        } else if (AgendaStatus.OPENED.equals(agendaModel.getStatus())) {
            agendaModel.add(linkTo(agendaController.vote(agendaDTO.getId(), new VoteRequest())).withRel("vote"));
        }
    }
}
