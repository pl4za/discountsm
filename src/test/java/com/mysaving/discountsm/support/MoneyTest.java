package com.mysaving.discountsm.support;

import static org.assertj.core.api.Assertions.assertThat;

import com.neovisionaries.i18n.CurrencyCode;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {

  @Test
  void itAddsMoney() {
    Money sourceMoney = Money.of(CurrencyCode.GBP, BigDecimal.ONE);
    Money moneyToAdd = Money.of(CurrencyCode.GBP, BigDecimal.ONE);

    assertThat(sourceMoney.add(moneyToAdd)).usingRecursiveComparison()
        .isEqualTo(Money.of(CurrencyCode.GBP, BigDecimal.valueOf(2)));
  }

  @Test
  void itSubtractsMoney() {
    Money sourceMoney = Money.of(CurrencyCode.GBP, BigDecimal.ONE);
    Money moneyToSub = Money.of(CurrencyCode.GBP, BigDecimal.ONE);

    assertThat(sourceMoney.sub(moneyToSub)).usingRecursiveComparison()
        .isEqualTo(Money.of(CurrencyCode.GBP, BigDecimal.ZERO));
  }
}