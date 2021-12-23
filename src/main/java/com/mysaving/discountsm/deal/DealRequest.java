package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.support.Money;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record DealRequest(
    @NotEmpty String title,
    @NotEmpty String description,
    @NotNull @Valid Money newPriceMoney,
    @NotNull @Valid Money oldPriceMoney,
    @NotEmpty String dealLink,
    @NotEmpty String imageLink) {
}


