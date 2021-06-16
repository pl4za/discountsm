package com.mysaving.discountsm.deal;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.then;
import static org.joda.money.CurrencyUnit.GBP;

import com.mysaving.discountsm.common.UUIDEntity;
import java.io.IOException;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DealsControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void itCanGetAllDeals() throws IOException {
    ResponseEntity<DealEntity[]> deals = this.testRestTemplate.getForEntity("http://localhost:" + this.port + "/deals", DealEntity[].class);

    // common properties
    then(deals.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(deals.getBody()).extracting(UUIDEntity::getId).isNotEmpty();

    // deal specific properties
    then(deals.getBody()).extracting(
        DealEntity::getTitle,
        DealEntity::getDescription,
        DealEntity::getNewPrice,
        DealEntity::getOldPrice,
        DealEntity::getScore,
        DealEntity::getPosted,
        DealEntity::getExpiry,
        DealEntity::getImage
    ).contains(
        tuple(
            "Untitled Goose Game for Nintendo Switch (Argos price match) +£3.99 non Prime",
            "Amazon have price matched Argos' price for this Nintendo Switch game. If you were after it from Argos and couldn't find stock before it sold out, this will hopefully be luckier for you! £3.99 shipping if you're not a Prime customer.",
            Money.of(GBP, 16.99),
            Money.of(GBP, 20.99),
            321,
            new DateTime(2021, 8, 16, 2, 30),
            new DateTime(2021, 8, 16, 2, 30).plusDays(5),
            "https://images.hotukdeals.com/threads/content/64CKj/3745735.jpg"
        ));
  }

}