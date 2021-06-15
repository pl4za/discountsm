package com.mysaving.discountsm.deal;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.then;

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
  public void itCanGetAllDeals() {
    ResponseEntity<Deal[]> deals = this.testRestTemplate.getForEntity("http://localhost:" + this.port + "/deals", Deal[].class);

    then(deals.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(deals.getBody()).extracting("title", "description")
        .containsOnly(tuple("title example", "description example"));
  }

}