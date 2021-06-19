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
@IdClass(UserVoteId.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_vote")
public class UserVoteEntity {

  @Id
  @JoinColumn(name = "user_id", table = "user", referencedColumnName = "id")
  private UUID userId;

  @Id
  @JoinColumn(name = "deal_id", table = "deal", referencedColumnName = "id")
  private UUID dealId;

  private int vote;
}


