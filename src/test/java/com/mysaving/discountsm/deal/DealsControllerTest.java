package com.mysaving.discountsm.deal;

import static org.assertj.core.api.BDDAssertions.then;
import static org.joda.money.CurrencyUnit.GBP;

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

  private static final DealEntity dealEntity = new DealEntity(
      "title",
      "description",
      Money.of(GBP, 10),
      Money.of(GBP, 11),
      10,
      5,
      new DateTime(2021, 8, 16, 2, 30),
      new DateTime(2021, 8, 16, 2, 30),
      "link",
      "image"
  );

  @Test
  public void itCanCreateDeal() {
    ResponseEntity<String> result = this.testRestTemplate.postForEntity(
        "http://localhost:" + this.port + "/deals", dealEntity, String.class);
    then(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    ResponseEntity<DealEntity> deal = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.port + "/deals/" + dealEntity.getId(), DealEntity.class);
    then(deal.getBody()).usingRecursiveComparison().isEqualTo(dealEntity);
  }

  @Test
  public void itCanUpdateDeal() {
    ResponseEntity<String> result = this.testRestTemplate.postForEntity(
        "http://localhost:" + this.port + "/deals", dealEntity, String.class);
    then(result.getStatusCode()).isEqualTo(HttpStatus.OK);

    DealEntity updatedDealEntity = new DealEntity(
        "new title",
        "new description",
        Money.of(GBP, 11),
        Money.of(GBP, 12),
        11,
        6,
        new DateTime(2021, 8, 16, 2, 30),
        new DateTime(2021, 8, 16, 2, 30),
        "new link",
        "new image"
    );
    this.testRestTemplate.put(
        "http://localhost:" + this.port + "/deals/" + dealEntity.getId(), updatedDealEntity);

    ResponseEntity<DealEntity> deal = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.port + "/deals/" + dealEntity.getId(), DealEntity.class);
    then(deal.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(updatedDealEntity);
  }

  @Test
  public void itCanGetAllDeals() {
    this.testRestTemplate.put(
        "http://localhost:" + this.port + "/deals", dealEntity);

    ResponseEntity<DealEntity[]> deals = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.port + "/deals", DealEntity[].class);
    then(deals.getBody()).hasOnlyOneElementSatisfying(entity -> then(entity).usingRecursiveComparison().isEqualTo(dealEntity));
  }
}