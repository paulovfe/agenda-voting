package com.paulovfe.agendavoting.service;

import com.paulovfe.agendavoting.dto.ResultDTO;
import com.paulovfe.agendavoting.dto.VoteDTO;
import com.paulovfe.agendavoting.entity.Agenda;
import com.paulovfe.agendavoting.exception.CPFAlreadyVotedException;
import com.paulovfe.agendavoting.exception.CPFUnableToVoteException;
import com.paulovfe.agendavoting.exception.ExpiredVoteSessionException;
import com.paulovfe.agendavoting.exception.InvalidStatusToVoteException;
import com.paulovfe.agendavoting.exception.NotFoundException;
import com.paulovfe.agendavoting.integration.httpclient.reponse.CPFStatus;
import com.paulovfe.agendavoting.mapper.AgendaMapper;
import com.paulovfe.agendavoting.mapper.VoteMapper;
import com.paulovfe.agendavoting.repository.AgendaRepository;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final UserInfoService userInfoService;
    private final VoteMapper voteMapper;
    private final AgendaMapper agendaMapper;
    private final AgendaRepository repository;

    public void doVote(final String id, final VoteDTO vote) {
        final Agenda agenda = repository.findById(new ObjectId(id))
                .orElseThrow(() -> new NotFoundException("Agenda not found!"));
        validateVote(agenda, vote);
        agenda.addVote(voteMapper.toEntity(vote));
        repository.save(agenda);
    }

    private void validateVote(final Agenda agenda, final VoteDTO vote) {
        if (!agenda.isOpened()) {
            throw new InvalidStatusToVoteException();
        }
        if (agenda.isExpired()) {
            throw new ExpiredVoteSessionException();
        }
        if (agenda.alreadyVoted(vote.getCpf())) {
            throw new CPFAlreadyVotedException();
        }
        validateCPF(vote.getCpf());
    }

    private void validateCPF(final String cpf) {
        userInfoService.getCpfStatus(cpf)
                
                .filter(CPFStatus.ABLE_TO_VOTE::equals)
                .orElseThrow(CPFUnableToVoteException::new);
    }

    public ResultDTO getResult(final String id) {
        return repository.findById(new ObjectId(id))
                .map(agendaMapper::toResult)
                .orElseThrow(() -> new NotFoundException("Agenda not found!"));
    }


}
