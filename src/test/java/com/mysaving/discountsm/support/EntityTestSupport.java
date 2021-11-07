package com.mysaving.discountsm.support;

import static org.assertj.core.api.BDDAssertions.then;
import static org.joda.money.CurrencyUnit.GBP;

import com.mysaving.discountsm.deal.DealEntity;
import com.mysaving.discountsm.person.PersonEntity;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EntityTestSupport {

  @Autowired
  public TestRestTemplate testRestTemplate;

  @LocalServerPort
  public int port;

  public DealEntity DEAL_ENTITY;
  public PersonEntity PERSON_ENTITY;

  @BeforeEach
  void setUp() {
    DEAL_ENTITY = new DealEntity(
        "title",
        "description",
        BigDecimal.valueOf(10),
        GBP.getCode(),
        BigDecimal.valueOf(11),
        GBP.getCode(),
        0,
        0,
        Instant.parse("2021-01-01T00:00:00Z"),
        Instant.parse("2021-01-01T00:00:00Z"),
        "link",
        "image"
    );
    PERSON_ENTITY = new PersonEntity("jason");
  }

  // CREATE
  public UUID givenAPerson() {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/people")
        .build()
        .toUri();
    ResponseEntity<String> peopleResponse = testRestTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(PERSON_ENTITY), String.class);
    then(peopleResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    return PERSON_ENTITY.getId();
  }

  public UUID givenADeal() {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals")
        .build()
        .toUri();
    ResponseEntity<String> dealResponse = testRestTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(DEAL_ENTITY), String.class);
    then(dealResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    return DEAL_ENTITY.getId();
  }

  // GET
  public DealEntity getDeal(UUID dealId) {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals/{dealId}")
        .buildAndExpand(dealId)
        .toUri();

    return testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), DealEntity.class).getBody();
  }

  public PersonEntity getPeople(UUID personId) {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/people/{personId}")
        .buildAndExpand(personId)
        .toUri();

    return testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), PersonEntity.class).getBody();
  }
}
