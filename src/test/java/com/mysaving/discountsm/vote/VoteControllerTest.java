package com.mysaving.discountsm.vote;

import static org.assertj.core.api.BDDAssertions.then;
import static org.joda.money.CurrencyUnit.GBP;

import com.mysaving.discountsm.deal.DealEntity;
import com.mysaving.discountsm.user.UserEntity;
import java.net.URI;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
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
class VoteControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Test
  public void itCanCreateUserVote() {
    // user
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/user")
        .build()
        .toUri();
    UserEntity userEntity = new UserEntity("jason");
    ResponseEntity<String> userResponse = testRestTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(userEntity), String.class);
    then(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    // deal
    uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals")
        .build()
        .toUri();
    DealEntity dealEntity = new DealEntity(
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
    ResponseEntity<String> dealResponse = testRestTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(dealEntity), String.class);
    then(dealResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    // vote
    uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/vote")
        .path("/deal/{dealId}")
        .path("/user/{userId}")
        .query("vote={vote}")
        .buildAndExpand(dealEntity.getId(), userEntity.getId(), 1)
        .toUri();
    ResponseEntity<String> voteCreateResponse = testRestTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(new HttpHeaders()), String.class);
    then(voteCreateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    // test
    uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/vote")
        .path("/deal/{dealId}")
        .path("/user/{userId}")
        .buildAndExpand(dealEntity.getId(), userEntity.getId())
        .toUri();
    ResponseEntity<UserVoteEntity> voteGetResponse = testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), UserVoteEntity.class);
    then(voteGetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(voteGetResponse.getBody()).usingRecursiveComparison().isEqualTo(new UserVoteEntity(userEntity.getId(), dealEntity.getId(), 1));
  }

}