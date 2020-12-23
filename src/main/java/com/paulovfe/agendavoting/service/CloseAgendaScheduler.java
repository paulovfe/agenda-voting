package com.paulovfe.agendavoting.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CloseAgendaScheduler {

    private final AgendaService agendaService;

    @Scheduled(cron = "${cron.close.agenda}")
    public void close() {
        log.info("Starting job ");
        agendaService.closeExpiredAgenda();
        log.info("Finish job");
    }

}
