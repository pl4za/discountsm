package com.mysaving.discountsm.user;

import static org.assertj.core.api.BDDAssertions.then;

import com.mysaving.discountsm.support.EntityTestSupport;
import java.net.URI;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

class UserControllerTest extends EntityTestSupport {

  @Test
  public void itCanCreateUser() {
    UUID userId = givenAUser();

    then(getUser(userId)).usingRecursiveComparison().ignoringFields("id").isEqualTo(USER_ENTITY);
  }

  @Test
  public void itCanCreateGoogleUser() {
    UUID googleUserId = createGoogleUser();
    then(googleUserId).isNotNull();
  }

  @Test
  public void itCanCreateGoogleUser_WhenUserAlreadyExists() {
    UUID googleUserId1 = createGoogleUser();
    UUID googleUserId2 = createGoogleUser();
    then(googleUserId1).isEqualTo(googleUserId2);
  }

  private UUID createGoogleUser() {
    GoogleUserEntity googleUserEntity = new GoogleUserEntity(
        USER_ENTITY.getId(),
        "jason costa",
        "jason@gmail.com",
        "email",
        "token"
    );

    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/users/auth/google")
        .build()
        .toUri();
    ResponseEntity<UUID> userResponse = testRestTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(googleUserEntity), UUID.class);
    then(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    return userResponse.getBody();
  }
}