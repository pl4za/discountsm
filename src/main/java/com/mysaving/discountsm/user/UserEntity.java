package com.mysaving.discountsm.user;

import com.mysaving.discountsm.common.UUIDEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends UUIDEntity {

  private String username;
}


