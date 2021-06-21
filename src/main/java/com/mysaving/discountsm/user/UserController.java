package com.mysaving.discountsm.user;

import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private GoogleUserRepository googleUserRepository;

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
  @Transactional
  @RequestMapping(value = "/auth/google", method = RequestMethod.PUT)
  public UUID createGoogleUser(@RequestBody @Valid GoogleUserEntity googleUserEntity) {
    return googleUserRepository.findById(googleUserEntity.getEmail())
        .map(GoogleUserEntity::getUserId)
        .orElseGet(() -> createNewGoogleUser(googleUserEntity));
  }

  private UUID createNewGoogleUser(GoogleUserEntity googleUserEntity) {
    UserEntity userId = userRepository.save(new UserEntity(googleUserEntity.getName().trim()));
    googleUserEntity.setUserId(userId.getId());
    googleUserRepository.save(googleUserEntity);
    return googleUserEntity.getUserId();
  }
}
