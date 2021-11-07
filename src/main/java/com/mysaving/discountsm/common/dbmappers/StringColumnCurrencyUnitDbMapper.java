package com.mysaving.discountsm.common.dbmappers;

import org.jadira.usertype.spi.shared.AbstractStringColumnMapper;
import org.joda.money.CurrencyUnit;

/**
 * A custom implementation to fix the mapping of CurrencyUnit to string
 */
public class StringColumnCurrencyUnitDbMapper extends AbstractStringColumnMapper<CurrencyUnit> {

  public CurrencyUnit fromNonNullValue(String s) {
    return CurrencyUnit.of(s);
  }

  public String toNonNullValue(CurrencyUnit value) {
    return value.getCode();
  }

}
