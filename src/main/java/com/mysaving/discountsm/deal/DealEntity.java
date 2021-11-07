package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.common.UUIDEntity;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.money.CurrencyUnit;
import org.joda.time.DateTime;

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

  private CurrencyUnit newPriceCurrency;

  private BigDecimal oldPriceAmount;

  private CurrencyUnit oldPriceCurrency;

  private int upVotes;

  private int downVotes;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime posted;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime expiry;

  private String dealLink;

  private String imageLink;
}


