package com.mysaving.discountsm.deal;

import static org.assertj.core.api.BDDAssertions.then;
import static org.joda.money.CurrencyUnit.GBP;

import com.mysaving.discountsm.support.EntityTestSupport;
import com.mysaving.discountsm.vote.PersonVoteEntity;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;
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
        BigDecimal.valueOf(11),
        GBP.getCode(),
        BigDecimal.valueOf(12),
        GBP.getCode(),
        11,
        6,
        Instant.parse("2021-01-01T00:00:00Z"),
        Instant.parse("2021-01-01T00:00:00Z"),
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
  public void itCanGetAllDealsWithPersonVote() {
    UUID personId = givenAPerson();
    UUID dealId = givenADeal();
    upVoteDeal(personId, dealId);

    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals")
        .path("/people/{personId}")
        .buildAndExpand(personId)
        .toUri();

    ResponseEntity<DealWithPersonVote[]> dealsResponse = testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), DealWithPersonVote[].class);
    then(dealsResponse.getBody()).extracting(DealWithPersonVote::getPersonVote).contains(1);
  }

  @Test
  public void itCanUpvoteDeal() {
    UUID personId = givenAPerson();
    UUID dealId = givenADeal();
    upVoteDeal(personId, dealId);

    ResponseEntity<DealEntity> deal = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.port + "/deals/" + dealId, DealEntity.class);
    then(deal.getBody()).extracting(DealEntity::getUpVotes).isEqualTo(DEAL_ENTITY.getUpVotes() + 1);
    then(deal.getBody()).extracting(DealEntity::getDownVotes).isEqualTo(DEAL_ENTITY.getDownVotes());

    // Voting up will reduce the upvote count as the person has voted up before
    downVoteDeal(personId, dealId);
    deal = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.port + "/deals/" + dealId, DealEntity.class);
    then(deal.getBody()).extracting(DealEntity::getUpVotes).isEqualTo(DEAL_ENTITY.getUpVotes());
    then(deal.getBody()).extracting(DealEntity::getDownVotes).isEqualTo(DEAL_ENTITY.getDownVotes() + 1);
  }

  @Test
  public void itCanDownVoteDeal() {
    UUID personId = givenAPerson();
    UUID dealId = givenADeal();
    downVoteDeal(personId, dealId);

    ResponseEntity<DealEntity> deal = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.port + "/deals/" + dealId, DealEntity.class);
    then(deal.getBody()).extracting(DealEntity::getUpVotes).isEqualTo(DEAL_ENTITY.getUpVotes());
    then(deal.getBody()).extracting(DealEntity::getDownVotes).isEqualTo(DEAL_ENTITY.getDownVotes() + 1);

    // Voting up will reduce the downVote count as the person has voted down before
    upVoteDeal(personId, dealId);

    deal = this.testRestTemplate.getForEntity(
        "http://localhost:" + this.port + "/deals/" + dealId, DealEntity.class);
    then(deal.getBody()).extracting(DealEntity::getUpVotes).isEqualTo(DEAL_ENTITY.getUpVotes() + 1);
    then(deal.getBody()).extracting(DealEntity::getDownVotes).isEqualTo(DEAL_ENTITY.getDownVotes());
  }

  private void upVoteDeal(UUID personId, UUID dealId) {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals/{dealId}")
        .path("/people/{personId}")
        .path("/up-vote")
        .buildAndExpand(dealId, personId)
        .toUri();
    ResponseEntity<String> voteCreateResponse = testRestTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(new HttpHeaders()), String.class);
    then(voteCreateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals/{dealId}")
        .path("/people/{personId}")
        .buildAndExpand(dealId, personId)
        .toUri();
    ResponseEntity<PersonVoteEntity> voteGetResponse = testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), PersonVoteEntity.class);
    then(voteGetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(voteGetResponse.getBody()).usingRecursiveComparison().isEqualTo(new PersonVoteEntity(personId, dealId, 1));
  }

  private void downVoteDeal(UUID personId, UUID dealId) {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals/{dealId}")
        .path("/people/{personId}")
        .path("/down-vote")
        .buildAndExpand(dealId, personId)
        .toUri();
    ResponseEntity<String> voteCreateResponse = testRestTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(new HttpHeaders()), String.class);
    then(voteCreateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/deals/{dealId}")
        .path("/people/{personId}")
        .buildAndExpand(dealId, personId)
        .toUri();
    ResponseEntity<PersonVoteEntity> voteGetResponse = testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), PersonVoteEntity.class);
    then(voteGetResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    then(voteGetResponse.getBody()).usingRecursiveComparison().isEqualTo(new PersonVoteEntity(personId, dealId, -1));
  }
}