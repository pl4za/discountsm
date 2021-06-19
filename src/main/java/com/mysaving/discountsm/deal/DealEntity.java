package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.common.UUIDEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
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

  @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
      parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "GBP")})
  private Money newPrice;

  @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
      parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "GBP")})
  private Money oldPrice;

  private int upVotes;

  private int downVotes;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime posted;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime expiry;

  private String link;

  private String image;
}


