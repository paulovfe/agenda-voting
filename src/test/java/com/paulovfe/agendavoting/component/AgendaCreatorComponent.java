package com.paulovfe.agendavoting.component;

import com.paulovfe.agendavoting.dto.AgendaDTO;
import com.paulovfe.agendavoting.dto.VoteDTO;
import com.paulovfe.agendavoting.entity.Option;
import com.paulovfe.agendavoting.repository.AgendaRepository;
import com.paulovfe.agendavoting.service.AgendaService;
import com.paulovfe.agendavoting.service.VoteService;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AgendaCreatorComponent {

    private static final int EXPIRATION = 10;
    private static final int RANDOM_COUNT = 10;

    private final AgendaService agendaService;
    private final VoteService voteService;
    private final AgendaRepository repository;

    public AgendaDTO create() {
        final AgendaDTO agenda = AgendaDTO.builder().name(RandomStringUtils.random(RANDOM_COUNT)).build();
        return agendaService.create(agenda);
    }

    public AgendaDTO createOpened() {
        final AgendaDTO agenda = AgendaDTO.builder().name(RandomStringUtils.random(RANDOM_COUNT)).build();
        return agendaService.doOpenVote(agendaService.create(agenda).getId(), EXPIRATION);
    }

    public AgendaDTO createOpenedAndVote(final int voteYes, final int voteNo) {
        final AgendaDTO opened = createOpened();
        vote(opened.getId(), voteYes, Option.YES);
        vote(opened.getId(), voteNo, Option.NO);
        return opened;
    }

    private void vote(final String id, final int count, final Option option) {
        IntStream.range(0, count).forEach(i -> {
            final String cpf = CPFHelper.generate();
            final VoteDTO vote = new VoteDTO();
            vote.setCpf(cpf);
            vote.setOption(option);
            MockUserInfoDetail.mockUserAble(cpf);
            voteService.doVote(id, vote);
        });
    }

    public Long currentCount() {
        return repository.count();
    }

}
