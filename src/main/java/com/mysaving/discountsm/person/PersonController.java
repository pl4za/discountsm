package com.mysaving.discountsm.person;

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
@RequestMapping("people")
public class PersonController {

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private GooglePersonRepository googlePersonRepository;
  @Autowired
  private GoogleTokenVerifier googleTokenVerifier;

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(method = RequestMethod.POST)
  public void createPerson(@RequestBody PersonEntity personEntity) {
    personRepository.save(personEntity);
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
  public Optional<PersonEntity> getPerson(@PathVariable(value = "personId") UUID personId) {
    return personRepository.findById(personId);
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/auth/google", method = RequestMethod.PUT)
  public UUID createGooglePerson(@Valid @NotBlank @RequestHeader("Authorization") String googleToken) {
    return validateTokenAndCreatePerson(googleToken);
  }

  private UUID validateTokenAndCreatePerson(String token) {
    try {
      Payload payload = googleTokenVerifier.validateToken(token)
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "UNABLE_TO_VALIDATE_PERSON"));

      return googlePersonRepository.findById(payload.getEmail())
          .map(GooglePersonEntity::getId)
          .orElseGet(() -> createNewGooglePerson(payload));
    } catch (Exception e) {
      e.printStackTrace();
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UNABLE_TO_VALIDATE_PERSON");
    }
  }

  @Transactional
  private UUID createNewGooglePerson(Payload payload) {
    String name = (String) payload.get("name");
    PersonEntity personId = personRepository.save(new PersonEntity(name.trim()));
    GooglePersonEntity googlePersonEntity = new GooglePersonEntity(
        personId.getId(),
        payload.getEmail(),
        name,
        (String) payload.get("picture"),
        (String) payload.get("locale"),
        (String) payload.get("given_name"),
        (String) payload.get("family_name")
    );
    return googlePersonRepository.save(googlePersonEntity).getId();
  }
}
