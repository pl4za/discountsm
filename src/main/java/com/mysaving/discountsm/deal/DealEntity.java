package com.mysaving.discountsm.deal;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Entity;
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

  private String newPriceCurrency;

  private BigDecimal oldPriceAmount;

  private String oldPriceCurrency;

  private int score;

  private Instant posted;

  private String dealLink;

  private String imageLink;
}


