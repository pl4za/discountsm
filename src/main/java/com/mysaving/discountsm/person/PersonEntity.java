package com.mysaving.discountsm.person;

import com.mysaving.discountsm.common.UUIDEntity;
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
@Table(name = "person")
@EqualsAndHashCode(callSuper = true)
public class PersonEntity extends UUIDEntity {

  private String username;
}


