package com.mysaving.discountsm.support;

import static org.assertj.core.api.BDDAssertions.then;
import static org.joda.money.CurrencyUnit.GBP;

import com.mysaving.discountsm.deal.DealEntity;
import com.mysaving.discountsm.user.UserEntity;
import java.net.URI;
import java.util.UUID;
import org.joda.money.Money;
import org.joda.time.DateTime;
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
  public UserEntity USER_ENTITY;

  @BeforeEach
  void setUp() {
    DEAL_ENTITY = new DealEntity(
        "title",
        "description",
        Money.of(GBP, 10),
        Money.of(GBP, 11),
        0,
        0,
        new DateTime(2021, 8, 16, 2, 30),
        new DateTime(2021, 8, 16, 2, 30),
        "link",
        "image"
    );
    USER_ENTITY = new UserEntity("jason");
  }

  // CREATE
  public UUID givenAUser() {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/users")
        .build()
        .toUri();
    ResponseEntity<String> userResponse = testRestTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(USER_ENTITY), String.class);
    then(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    return USER_ENTITY.getId();
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

  public UserEntity getUser(UUID userId) {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/users/{userId}")
        .buildAndExpand(userId)
        .toUri();

    return testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), UserEntity.class).getBody();
  }
}
