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
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    UUID transactionUid = response.getBody();
    assertThat(transactionUid).isNotNull();

    then(dealRepository.findById(transactionUid)).isPresent();
  }
}