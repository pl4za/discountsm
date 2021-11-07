package com.mysaving.discountsm.person;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "person_google")
public class GooglePersonEntity {

  @JoinColumn(name = "person_id", table = "person", referencedColumnName = "id", nullable = false, unique = true)
  private UUID id;

  @Id
  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = true)
  private String image;

  @Column(nullable = true)
  private String locale;

  @Column(nullable = true)
  private String givenName;

  @Column(nullable = true)
  private String familyName;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    GooglePersonEntity that = (GooglePersonEntity) o;
    return email != null && Objects.equals(email, that.email);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}


