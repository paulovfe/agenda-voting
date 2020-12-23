package com.paulovfe.agendavoting.mapper;

import com.paulovfe.agendavoting.api.request.VoteRequest;
import com.paulovfe.agendavoting.dto.VoteDTO;
import com.paulovfe.agendavoting.entity.Vote;

import org.mapstruct.Mapper;

@Mapper
public interface VoteMapper {
    VoteDTO toDTO(VoteRequest vote);

    Vote toEntity(VoteDTO vote);
}
