package com.mysaving.discountsm.deal;

import com.neovisionaries.i18n.CurrencyCode;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "deal")
public class DealEntity {

  @Id
  private UUID id;

  private String title;

  private String description;

  private BigDecimal newPriceAmount;

  @Enumerated(EnumType.STRING)
  private CurrencyCode newPriceCurrency;

  private BigDecimal oldPriceAmount;

  @Enumerated(EnumType.STRING)
  private CurrencyCode oldPriceCurrency;

  private int upVotes;

  private int downVotes;

  private ZonedDateTime posted;

  private ZonedDateTime expiry;

  private String dealLink;

  private String imageLink;
}


