package com.mysaving.discountsm.vote;

import com.mysaving.discountsm.deal.DealRepository;
import com.mysaving.discountsm.user.UserRepository;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("vote")
public class VoteController {

  @Autowired
  private VoteRepository voteRepository;
  @Autowired
  private DealRepository dealRepository;
  @Autowired
  private UserRepository userRepository;

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/deal/{dealId}/user/{userId}", method = RequestMethod.GET)
  public Optional<UserVoteEntity> getVote(
      @PathVariable(value = "dealId") UUID dealId,
      @PathVariable(value = "userId") UUID userId) {
    return voteRepository.findById(new UserVoteId(userId, dealId));
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @Transactional
  @RequestMapping(value = "/deal/{dealId}/user/{userId}", method = RequestMethod.PUT)
  public void updateVote(
      @PathVariable(value = "dealId") UUID dealId,
      @PathVariable(value = "userId") UUID userId,
      @RequestParam @NotNull @Size(min = -1, max = 1) int vote) {

    if (userRepository.findById(userId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }

    if (dealRepository.findById(dealId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DEAL_NOT_FOUND");
    }

    UserVoteId userVoteId = new UserVoteId(userId, dealId);
    Optional<UserVoteEntity> userVote = voteRepository.findById(userVoteId);
    userVote.ifPresentOrElse(
        uv -> uv.setVote(vote), // update
        () -> voteRepository.save(new UserVoteEntity(userId, dealId, vote)) // create
    );
  }
}
