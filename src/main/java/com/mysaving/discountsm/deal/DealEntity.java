package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.common.UUIDEntity;
import com.mysaving.discountsm.common.dbmappers.CurrencyUnitDbMapper;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.money.CurrencyUnit;
import org.joda.time.DateTime;

@Getter
@Setter
@ToString
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor@Table(name = "deal")
public class DealEntity extends UUIDEntity {

  private String title;

  private String description;

  private BigDecimal newPriceAmount;

  @Type(type = CurrencyUnitDbMapper.CLASS_NAME)
  private CurrencyUnit newPriceCurrency;

  private BigDecimal oldPriceAmount;

  @Type(type = CurrencyUnitDbMapper.CLASS_NAME)
  private CurrencyUnit oldPriceCurrency;

  private int upVotes;

  private int downVotes;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime posted;

  @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime expiry;

  private String dealLink;

  private String imageLink;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    DealEntity that = (DealEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}


