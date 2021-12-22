package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.support.Money;
import java.time.ZonedDateTime;
import java.util.UUID;

public record DealResponse(
    UUID id,
    String title,
    String description,
    Money newPriceMoney,
    Money oldPriceMoney,
    int upVotes,
    int downVotes,
    ZonedDateTime created,
    ZonedDateTime expiry,
    String dealLink,
    String imageLink) {
}


