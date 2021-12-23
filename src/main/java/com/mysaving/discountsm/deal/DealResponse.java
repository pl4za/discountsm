package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.support.Money;
import java.time.Instant;
import java.util.UUID;

public record DealResponse(
    UUID id,
    String title,
    String description,
    Money newPriceMoney,
    Money oldPriceMoney,
    int score,
    Instant posted,
    String dealLink,
    String imageLink) {
}


