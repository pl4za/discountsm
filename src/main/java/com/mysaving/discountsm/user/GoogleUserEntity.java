package com.mysaving.discountsm.user;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Entity
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_google")
public class GoogleUserEntity {

  @JoinColumn(name = "user_id", table = "user", referencedColumnName = "id", nullable = false, unique = true)
  private UUID userId;

  @NotNull
  @Column(nullable = false)
  private String name;

  @Id
  @Email
  @NotNull
  @Column(nullable = false, unique = true)
  private String email;

  @NotNull
  @Column(nullable = false)
  private String image;

  @NotNull
  @Column(nullable = false)
  private String token;
}


