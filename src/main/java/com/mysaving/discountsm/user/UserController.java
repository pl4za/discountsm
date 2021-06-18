package com.mysaving.discountsm.user;

import java.util.Optional;
import java.util.UUID;
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
}
