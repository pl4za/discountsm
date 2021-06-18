package com.mysaving.discountsm.user;

import static org.assertj.core.api.BDDAssertions.then;

import com.mysaving.discountsm.support.EntityTestSupport;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserControllerTest extends EntityTestSupport {

  @Test
  public void itCanCreateUser() {
    UUID userId = givenAUser();

    then(getUser(userId)).usingRecursiveComparison().ignoringFields("id").isEqualTo(USER_ENTITY);
  }

}