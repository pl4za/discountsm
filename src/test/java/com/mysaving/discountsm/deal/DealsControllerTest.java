package com.mysaving.discountsm.deal;

import static org.assertj.core.api.BDDAssertions.then;
import static org.joda.money.CurrencyUnit.GBP;

import com.mysaving.discountsm.support.EntityTestSupport;
import com.mysaving.discountsm.vote.UserVoteEntity;
import java.net.URI;
import java.util.UUID;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

class DealsControllerTest extends EntityTestSupport {

  @Test
  public void itCanCreateDeal() {
    UUID dealId = givenADeal();

    then(getDeal(dealId)).usingRecursiveComparison().ignoringFields("id").isEqualTo(DEAL_ENTITY);
  }

  @Test
  public void itCanUpdateDeal() {
    UUID dealId = givenADeal();

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

    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals/{dealId}")
        .buildAndExpand(dealId)
        .toUri();

    testRestTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(updatedDealEntity), DealEntity.class);

    then(getDeal(dealId)).usingRecursiveComparison().ignoringFields("id").isEqualTo(updatedDealEntity);
  }

  @Test
  public void itCanGetAllDeals() {
    UUID dealId = givenADeal();
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals")
        .build()
        .toUri();

    ResponseEntity<DealEntity[]> dealsResponse = testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), DealEntity[].class);
    then(dealsResponse.getBody()).extracting(DealEntity::getId).contains(dealId);
  }

  @Test
  public void itCanGetAllDealsWithUserVote() {
    UUID userId = givenAUser();
    UUID dealId = givenADeal();
    upVoteDeal(userId, dealId);

    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals")
        .path("/users/{userId}")
        .buildAndExpand(userId)
        .toUri();

    ResponseEntity<DealWithUserVote[]> dealsResponse = testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), DealWithUserVote[].class);
    then(dealsResponse.getBody()).extracting(DealWithUserVote::getUserVote).contains(1);
  }

  @Test
  public void itCanUpvoteDeal() {
    UUID userId = givenAUser();
    UUID dealId = givenADeal();
    upVoteDeal(userId, dealId);

    ResponseEntity<DealEntity> deal = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.port + "/deals/" + dealId, DealEntity.class);
    then(deal.getBody()).extracting(DealEntity::getUpVotes).isEqualTo(DEAL_ENTITY.getUpVotes() + 1);
    then(deal.getBody()).extracting(DealEntity::getDownVotes).isEqualTo(DEAL_ENTITY.getDownVotes());

    // Voting up will reduce the upvote count as the user has voted up before
    downVoteDeal(userId, dealId);
    deal = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.port + "/deals/" + dealId, DealEntity.class);
    then(deal.getBody()).extracting(DealEntity::getUpVotes).isEqualTo(DEAL_ENTITY.getUpVotes());
    then(deal.getBody()).extracting(DealEntity::getDownVotes).isEqualTo(DEAL_ENTITY.getDownVotes() + 1);
  }

  @Test
  public void itCanDownVoteDeal() {
    UUID userId = givenAUser();
    UUID dealId = givenADeal();
    downVoteDeal(userId, dealId);

    ResponseEntity<DealEntity> deal = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.port + "/deals/" + dealId, DealEntity.class);
    then(deal.getBody()).extracting(DealEntity::getUpVotes).isEqualTo(DEAL_ENTITY.getUpVotes());
    then(deal.getBody()).extracting(DealEntity::getDownVotes).isEqualTo(DEAL_ENTITY.getDownVotes() + 1);

    // Voting up will reduce the downVote count as the user has voted down before
    upVoteDeal(userId, dealId);

    deal = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.port + "/deals/" + dealId, DealEntity.class);
    then(deal.getBody()).extracting(DealEntity::getUpVotes).isEqualTo(DEAL_ENTITY.getUpVotes() + 1);
    then(deal.getBody()).extracting(DealEntity::getDownVotes).isEqualTo(DEAL_ENTITY.getDownVotes());
  }

  private void upVoteDeal(UUID userId, UUID dealId) {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals/{dealId}")
        .path("/users/{userId}")
        .path("/up-vote")
        .buildAndExpand(dealId, userId)
        .toUri();
    ResponseEntity<String> voteCreateResponse = testRestTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(new HttpHeaders()), String.class);
    then(voteCreateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals/{dealId}")
        .path("/users/{userId}")
        .buildAndExpand(dealId, userId)
        .toUri();
    ResponseEntity<UserVoteEntity> voteGetResponse = testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), UserVoteEntity.class);
    then(voteGetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(voteGetResponse.getBody()).usingRecursiveComparison().isEqualTo(new UserVoteEntity(userId, dealId, 1));
  }

  private void downVoteDeal(UUID userId, UUID dealId) {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals/{dealId}")
        .path("/users/{userId}")
        .path("/down-vote")
        .buildAndExpand(dealId, userId)
        .toUri();
    ResponseEntity<String> voteCreateResponse = testRestTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(new HttpHeaders()), String.class);
    then(voteCreateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals/{dealId}")
        .path("/users/{userId}")
        .buildAndExpand(dealId, userId)
        .toUri();
    ResponseEntity<UserVoteEntity> voteGetResponse = testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), UserVoteEntity.class);
    then(voteGetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(voteGetResponse.getBody()).usingRecursiveComparison().isEqualTo(new UserVoteEntity(userId, dealId, -1));
  }
}