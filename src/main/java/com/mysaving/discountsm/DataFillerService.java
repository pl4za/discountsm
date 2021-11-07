package com.mysaving.discountsm;

import static org.joda.money.CurrencyUnit.GBP;

import com.mysaving.discountsm.deal.DealEntity;
import com.mysaving.discountsm.deal.DealRepository;
import com.mysaving.discountsm.person.PersonEntity;
import com.mysaving.discountsm.person.PersonRepository;
import com.mysaving.discountsm.vote.VoteRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataFillerService {

  @Autowired
  private DealRepository dealRepository;

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private VoteRepository voteRepository;

  @PostConstruct
  @Transactional
  public void fillData() {
    // deals
    DealEntity dealEntity = new DealEntity(
        "Untitled Goose Game for Nintendo Switch (Argos price match) +£3.99 non Prime",
        "Amazon have price matched Argos' price for this Nintendo Switch game. If you were after it from Argos and couldn't find stock before it sold out, this will hopefully be luckier for you! £3.99 shipping if you're not a Prime customer.",
        BigDecimal.valueOf(16.99),
        GBP,
        BigDecimal.valueOf(20.99),
        GBP,
        0,
        0,
        new DateTime(2021, 8, 16, 2, 30),
        new DateTime(2021, 8, 16, 2, 30).plusDays(5),
        "https://www.currys.co.uk/gbuk/gaming/console-gaming/consoles/nintendo-game-watch-super-mario-bros-10218185-pdt.html",
        "https://images.hotukdeals.com/threads/content/64CKj/3745735.jpg"
    );

    DealEntity dealEntity2 = new DealEntity(
        "Adidas Originals ZX700 Trainers + free lettering + free delivery £32.72 vwith code @ Adidas Shop",
        "Get a unique code hereAdidas Originals ZX700 +free lettering+free delivery £32.72 with code\n"
            + "Red only\n"
            + "All sizes available.",
        BigDecimal.valueOf(10.99),
        GBP,
        BigDecimal.valueOf(19.99),
        GBP,
        0,
        0,
        new DateTime(2021, 8, 16, 2, 30),
        new DateTime(2021, 8, 16, 2, 30).plusDays(5),
        "https://www.currys.co.uk/gbuk/gaming/console-gaming/consoles/nintendo-game-watch-super-mario-bros-10218185-pdt.html",
        "https://images.hotukdeals.com/thread_additional_info/content/QiT4e/44711.jpg"
    );

    dealRepository.saveAll(List.of(dealEntity, dealEntity2));

    // person and vote
    PersonEntity personEntity = new PersonEntity("jason_" + UUID.randomUUID());
    personRepository.save(personEntity);
  }
}