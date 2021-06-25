package com.mysaving.discountsm.user;

import static org.assertj.core.api.BDDAssertions.then;

import com.mysaving.discountsm.support.EntityTestSupport;
import java.net.URI;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

class UserControllerTest extends EntityTestSupport {

  private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjExMmU0YjUyYWI4MzMwMTdkMzg1Y2UwZDBiNGM2MDU4N2VkMjU4NDIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiYXpwIjoiMjg0MDQ1OTczNzQtOHF2NGIwMG1tYzJ1cjQxbXJxczduNXJjZGVrdDd2MnAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIyODQwNDU5NzM3NC04cXY0YjAwbW1jMnVyNDFtcnFzN241cmNkZWt0N3YycC5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjExNTE4MjM1NTI2ODA4OTY0MjkzOSIsImVtYWlsIjoiamFzb25waGlsaXBzYXJkb2Nvc3RhQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiQklPcHdxMmozUXZrWlZzQWlkVDVmZyIsIm5hbWUiOiJKYXNvbiBQaGlsaXAiLCJwaWN0dXJlIjoiaHR0cHM6Ly9saDMuZ29vZ2xldXNlcmNvbnRlbnQuY29tL2EtL0FPaDE0R2oxUG9JY1lLbFpkQWFENXFlQWtOVUh1TEdQdVk3cnZKY01XU3JGYzAwPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6Ikphc29uIiwiZmFtaWx5X25hbWUiOiJQaGlsaXAiLCJsb2NhbGUiOiJlbi1HQiIsImlhdCI6MTYyNDYyNzA3NywiZXhwIjoxNjI0NjMwNjc3LCJqdGkiOiIwOGU1NzY5MjI5NDc1YzFkMDc3ODYzYmY4ODQ0ZTQ4YTM5M2I3OGRlIn0.Jk8iZ8jxcR2n1VVexoKClKhfy42RpQGVTVqok_wtAjaMihJHPC62-dQ3dHRDRqFbOiTiejz5JHtxVnQ55dehsYrXcINx3kqx9ktvOHMZ0-3l89Pd_VZqKAurSMm87y-1lYq2qL8QIDAJwd21FEfIuXHXQqsf86gh2gV7WrGxhcHiYkZ5HQQA-s9nlb23vJz2OQH6qgDyNiam0OSqQINz7HD_lApKdsG7u73sAA23zOdVyxaVwPKcfOHqzbGfwozckUcNLdihQ_tylZvqs8ak1eCOBuiuqjNdyjcVzNtB6YFV7Sl1sEUpxmcRv8xsI1yUgQdyish8soh2zMZqmaEjPg";

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
    URI uri = UriComponentsBuilder.newInstance()
        .scheme("http")
        .host("localhost")
        .port(port)
        .path("/users/auth/google")
        .build()
        .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", TOKEN);

    ResponseEntity<UUID> userResponse = testRestTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(headers), UUID.class);
    then(userResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    return userResponse.getBody();
  }
}