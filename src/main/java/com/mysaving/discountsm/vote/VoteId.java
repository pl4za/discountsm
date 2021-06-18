package com.mysaving.discountsm.vote;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.UUID;

public class VoteId implements Serializable {

  private UUID userId;
  private UUID dealId;

  public VoteId(UUID userId, UUID dealId) {
    this.userId = userId;
    this.dealId = dealId;
  }

  public UUID getUserId() {
    return userId;
  }

  public UUID getDealIs() {
    return dealId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public void setDealId(UUID dealId) {
    this.dealId = dealId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }
    VoteId voteId = (VoteId) o;
    return Objects.equal(userId, voteId.userId) && Objects.equal(dealId, voteId.dealId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(userId, dealId);
  }
}


