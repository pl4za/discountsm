package com.mysaving.discountsm.vote;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<PersonVoteEntity, PersonVoteId> {}
