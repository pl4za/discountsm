package com.mysaving.discountsm.vote;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.UUID;

public class PersonVoteId implements Serializable {

  private UUID personId;
  private UUID dealId;

  public PersonVoteId() {}

  public PersonVoteId(UUID PersonId, UUID dealId) {
    this.personId = PersonId;
    this.dealId = dealId;
  }

  public UUID getPersonId() {
    return personId;
  }

  public UUID getDealIs() {
    return dealId;
  }

  public void setPersonId(UUID personId) {
    this.personId = personId;
  }

  public void setDealId(UUID dealId) {
    this.dealId = dealId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }
    PersonVoteId personVoteId = (PersonVoteId) o;
    return Objects.equal(personId, personVoteId.personId) && Objects.equal(dealId, personVoteId.dealId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(personId, dealId);
  }
}


