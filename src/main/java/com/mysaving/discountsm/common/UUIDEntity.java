package com.mysaving.discountsm.common;

import java.util.UUID;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.Type;

@MappedSuperclass
public class UUIDEntity {

  @Id
  @Type(type = "pg-uuid")
  private UUID id;

  public UUIDEntity() {
    this.id = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }
}
