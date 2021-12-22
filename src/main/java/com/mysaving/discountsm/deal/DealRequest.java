package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.support.Money;
import java.time.Instant;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record DealRequest(
    @NotEmpty String title,
    @NotEmpty String description,
    @NotNull @Valid Money newPriceMoney,
    @NotNull @Valid Money oldPriceMoney,
    @NotNull Instant expiry,
    @NotEmpty String dealLink,
    @NotEmpty String imageLink) {
}


