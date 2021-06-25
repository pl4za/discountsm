package com.mysaving.discountsm.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("users")
public class UserController {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private GoogleUserRepository googleUserRepository;
  @Autowired
  private GoogleTokenVerifier googleTokenVerifier;

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(method = RequestMethod.POST)
  public void createUser(@RequestBody UserEntity userEntity) {
    userRepository.save(userEntity);
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
  public Optional<UserEntity> getUser(@PathVariable(value = "userId") UUID userId) {
    return userRepository.findById(userId);
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/auth/google", method = RequestMethod.PUT)
  public UUID createGoogleUser(@Valid @NotBlank @RequestHeader("Authorization") String googleToken) {
    return validateTokenAndCreateUser(googleToken);
  }

  private UUID validateTokenAndCreateUser(String token) {
    try {
      Payload payload = googleTokenVerifier.validateToken(token)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "UNABLE_TO_VALIDATE_USER"));

      return googleUserRepository.findById(payload.getEmail())
          .map(GoogleUserEntity::getUserId)
          .orElseGet(() -> createNewGoogleUser(payload));
    } catch (Exception e) {
      e.printStackTrace();
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UNABLE_TO_VALIDATE_USER");
    }
  }

  @Transactional
  private UUID createNewGoogleUser(Payload payload) {
    String name = (String) payload.get("name");
    UserEntity userId = userRepository.save(new UserEntity(name.trim()));
    GoogleUserEntity googleUserEntity = new GoogleUserEntity(
        userId.getId(),
        payload.getEmail(),
        name,
        (String) payload.get("picture"),
        (String) payload.get("locale"),
        (String) payload.get("given_name"),
        (String) payload.get("family_name")
    );
    return googleUserRepository.save(googleUserEntity).getUserId();
  }
}
