package com.mysaving.discountsm.deal;

import static com.neovisionaries.i18n.CurrencyCode.GBP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import com.mysaving.discountsm.repository.DealRepository;
import com.mysaving.discountsm.support.Money;
import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DealResourceIT {

  @Autowired
  public TestRestTemplate testRestTemplate;

  @Autowired
  private DealRepository dealRepository;

  @LocalServerPort
  public int port;

  @Test
  public void itCanCreateDeal() {
    UUID dealUid = givenADeal();
    assertThat(dealUid).isNotNull();
    then(dealRepository.findById(dealUid)).isPresent();
  }

  @Test
  public void itCanGetAllDeals() {
    UUID dealUid1 = givenADeal();
    UUID dealUid2 = givenADeal();
    assertThat(dealUid1).isNotNull();
    assertThat(dealUid2).isNotNull();

    List<DealResponse> deals = getAllDeals();
    then(deals).extracting(DealResponse::id).contains(dealUid1, dealUid2);
  }

  @Test
  void itCanVoteOnDeals() {
    UUID dealUid1 = givenADeal();
    UUID dealUid2 = givenADeal();
    assertThat(dealUid1).isNotNull();
    assertThat(dealUid2).isNotNull();

    List<DealResponse> deals = getAllDeals();
    then(deals).extracting(DealResponse::id, DealResponse::upVotes, DealResponse::downVotes)
        .contains(
            new Tuple(dealUid1, 0, 0),
            new Tuple(dealUid2, 0, 0)
        );

    int score1 = voteOnDeal(dealUid1, "up-vote");
    int score2 = voteOnDeal(dealUid2, "down-vote");
    assertThat(score1).isEqualTo(1);
    assertThat(score2).isEqualTo(-1);

    deals = getAllDeals();
    then(deals).extracting(DealResponse::id, DealResponse::upVotes, DealResponse::downVotes)
        .contains(
            new Tuple(dealUid1, 1, 0),
            new Tuple(dealUid2, 0, 1)
        );
  }

  private int voteOnDeal(UUID dealUid1, String path) {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("api/v1/deal/{dealUid}/" + path)
        .buildAndExpand(dealUid1)
        .toUri();

    ResponseEntity<Integer> response = testRestTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(new HttpHeaders()), Integer.class);
    return response.getBody();
  }

  private List<DealResponse> getAllDeals() {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("api/v1/deal")
        .build()
        .toUri();

    ResponseEntity<DealResponse[]> response = testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), DealResponse[].class);
    return Arrays.asList(response.getBody());
  }

  private UUID givenADeal() {
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("api/v1/deal")
        .build()
        .toUri();

    DealRequest dealRequest = new DealRequest(
        "title", "desc", Money.of(GBP, BigDecimal.ONE),
        Money.of(GBP, BigDecimal.TEN), Instant.now().plus(1, ChronoUnit.DAYS),
        "https://tinyurl.com/2p838csd",
        "https://images.hotukdeals.com/threads/raw/default/3857523_1/re/1024x1024/qt/60/3857523_1.jpg"
    );

    ResponseEntity<UUID> response = testRestTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(dealRequest), UUID.class);
    return response.getBody();
  }
}