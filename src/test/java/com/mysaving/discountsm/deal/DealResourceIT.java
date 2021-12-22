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

    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("api/v1/deal")
        .build()
        .toUri();

    ResponseEntity<DealResponse[]> response = testRestTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), DealResponse[].class);
    List<DealResponse> deals = Arrays.asList(response.getBody());

    then(deals).extracting(DealResponse::id).contains(dealUid1, dealUid2);
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
        "dealLink", "imageLink"
    );

    ResponseEntity<UUID> response = testRestTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(dealRequest), UUID.class);
    return response.getBody();
  }
}