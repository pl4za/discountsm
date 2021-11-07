package com.mysaving.discountsm.common.dbmappers;

import org.jadira.usertype.spi.shared.AbstractSingleColumnUserType;
import org.joda.money.CurrencyUnit;

public class CurrencyUnitDbMapper extends AbstractSingleColumnUserType<CurrencyUnit, String, StringColumnCurrencyUnitDbMapper> {

  public static final String CLASS_NAME = "com.mysaving.discountsm.common.dbmappers.CurrencyUnitDbMapper";
}

