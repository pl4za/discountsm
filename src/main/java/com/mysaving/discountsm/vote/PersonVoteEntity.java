package com.mysaving.discountsm.vote;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@ToString
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@IdClass(PersonVoteId.class)
@Table(name = "person_vote")
public class PersonVoteEntity {

  @Id
  @JoinColumn(name = "person_id", table = "person", referencedColumnName = "id")
  private UUID personId;

  @Id
  @JoinColumn(name = "deal_id", table = "deal", referencedColumnName = "id")
  private UUID dealId;

  private int vote;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    PersonVoteEntity that = (PersonVoteEntity) o;
    return personId != null && Objects.equals(personId, that.personId)
        && dealId != null && Objects.equals(dealId, that.dealId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(personId, dealId);
  }
}


