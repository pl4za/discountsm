package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.common.UUIDEntity;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deal")
@EqualsAndHashCode(callSuper = true)
public class DealEntity extends UUIDEntity {

  private String title;

  private String description;

  private BigDecimal newPriceAmount;

  private String newPriceCurrency;

  private BigDecimal oldPriceAmount;

  private String oldPriceCurrency;

  private int upVotes;

  private int downVotes;

  private Instant posted;

  private Instant expiry;

  private String dealLink;

  private String imageLink;
}


