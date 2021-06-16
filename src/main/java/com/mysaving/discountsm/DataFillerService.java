package com.mysaving.discountsm;

import static org.joda.money.CurrencyUnit.GBP;

import com.mysaving.discountsm.deal.DealEntity;
import com.mysaving.discountsm.deal.DealRepository;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataFillerService {

  @Autowired
  private DealRepository dealRepository;

  @PostConstruct
  @Transactional
  public void fillData() {
    DealEntity dealEntity = new DealEntity(
        "Untitled Goose Game for Nintendo Switch (Argos price match) +£3.99 non Prime",
        "Amazon have price matched Argos' price for this Nintendo Switch game. If you were after it from Argos and couldn't find stock before it sold out, this will hopefully be luckier for you! £3.99 shipping if you're not a Prime customer.",
        Money.of(GBP, 16.99),
        Money.of(GBP, 20.99),
        0,
        new DateTime(2014, 12, 20, 2, 30, DateTimeZone.forID("Europe/London")),
        new DateTime(2014, 12, 20, 2, 30, DateTimeZone.forID("Europe/London")).plusDays(5)
    );

    dealRepository.save(dealEntity);
  }
}