package com.paulovfe.agendavoting.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import static com.paulovfe.agendavoting.entity.AgendaStatus.CLOSED;
import static com.paulovfe.agendavoting.entity.AgendaStatus.OPENED;

@Data
@Document(collection = "agenda")
public class Agenda {

    @Id
    private ObjectId id;
    private String name;
    private AgendaStatus status;

    @CreatedDate
    private Instant createdDate;

    private Instant expirationDate;
    private List<Vote> votes;

    public List<Vote> getVotes() {
        if (votes == null) {
            votes = new ArrayList<>();
        }
        return votes;
    }

    public boolean alreadyVoted(final String cpf) {
        return getVotes().stream().anyMatch(voted -> voted.getCpf().equals(cpf));
    }

    public boolean isExpired() {
        return Instant.now().isAfter(getExpirationDate());
    }

    public boolean isOpened() {
        return OPENED.equals(getStatus());
    }

    public boolean isClosed() {
        return CLOSED.equals(getStatus());
    }

    public void addVote(final Vote vote) {
        getVotes().add(vote);
    }
}
