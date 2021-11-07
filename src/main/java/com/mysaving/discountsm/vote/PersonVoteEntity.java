package com.mysaving.discountsm.vote;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@IdClass(PersonVoteId.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "person_vote")
public class PersonVoteEntity {

  @Id
  @JoinColumn(name = "person_id", table = "person", referencedColumnName = "id")
  private UUID personId;

  @Id
  @JoinColumn(name = "deal_id", table = "deal", referencedColumnName = "id")
  private UUID dealId;

  private int vote;
}


