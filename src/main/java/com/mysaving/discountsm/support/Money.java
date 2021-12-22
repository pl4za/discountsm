package com.mysaving.discountsm.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mysaving.discountsm.exception.CurrencyMismatchException;
import com.neovisionaries.i18n.CurrencyCode;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public record Money(
    @NotNull @JsonProperty("currencyCode") CurrencyCode currencyCode,
    @NotNull @Positive @JsonProperty("amount") BigDecimal amount) {

  public static Money of(CurrencyCode currencyCode, BigDecimal amount) {
    return new Money(currencyCode, amount.setScale(2, RoundingMode.UP));
  }

  Money add(Money moneyToAdd) {
    if (!currencyCode.equals(moneyToAdd.currencyCode)) {
      throw new CurrencyMismatchException();
    }
    BigDecimal newAmount = this.amount.add(moneyToAdd.amount);
    return Money.of(this.currencyCode, newAmount);
  }

  Money sub(Money moneyToSub) {
    if (!currencyCode.equals(moneyToSub.currencyCode)) {
      throw new CurrencyMismatchException();
    }
    BigDecimal newAmount = this.amount.subtract(moneyToSub.amount);
    return Money.of(this.currencyCode, newAmount);
  }
}
