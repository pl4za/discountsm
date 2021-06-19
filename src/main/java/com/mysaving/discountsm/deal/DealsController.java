package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.user.UserRepository;
import com.mysaving.discountsm.vote.UserVoteEntity;
import com.mysaving.discountsm.vote.UserVoteId;
import com.mysaving.discountsm.vote.VoteRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("deals")
public class DealsController {

  @Autowired
  private VoteRepository voteRepository;
  @Autowired
  private DealRepository dealRepository;
  @Autowired
  private UserRepository userRepository;

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/{dealId}", method = RequestMethod.GET)
  public Optional<DealEntity> getDeal(@PathVariable(value = "dealId") UUID dealId) {
    return dealRepository.findById(dealId);
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(method = RequestMethod.POST)
  public void createDeal(@RequestBody DealEntity dealEntity) {
    dealRepository.save(dealEntity);
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @Transactional
  @RequestMapping(value = "/{dealId}", method = RequestMethod.PUT)
  public void updateDeal(@PathVariable(value = "dealId") UUID dealId, @RequestBody DealEntity dealEntity) {
    dealRepository.findById(dealId).ifPresent(existingEntity -> {
      existingEntity.setTitle(dealEntity.getTitle());
      existingEntity.setDescription(dealEntity.getDescription());
      existingEntity.setNewPrice(dealEntity.getNewPrice());
      existingEntity.setOldPrice(dealEntity.getOldPrice());
      existingEntity.setUpVotes(dealEntity.getUpVotes());
      existingEntity.setDownVotes(dealEntity.getDownVotes());
      existingEntity.setPosted(dealEntity.getPosted());
      existingEntity.setExpiry(dealEntity.getExpiry());
      existingEntity.setLink(dealEntity.getLink());
      existingEntity.setImage(dealEntity.getImage());
    });
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(method = RequestMethod.GET)
  public List<DealEntity> getAllDeals() {
    return dealRepository.findAll();
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/{dealId}/user/{userId}", method = RequestMethod.GET)
  public Optional<UserVoteEntity> getVote(
      @PathVariable(value = "dealId") UUID dealId,
      @PathVariable(value = "userId") UUID userId) {
    return voteRepository.findById(new UserVoteId(userId, dealId));
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/{dealId}/user/{userId}/up-vote", method = RequestMethod.PUT)
  public void upvoteDeal(
      @PathVariable(value = "dealId") UUID dealId,
      @PathVariable(value = "userId") UUID userId
  ) {
    vote(dealId, userId, 1);
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/{dealId}/user/{userId}/down-vote", method = RequestMethod.PUT)
  public void downVoteDeal(
      @PathVariable(value = "dealId") UUID dealId,
      @PathVariable(value = "userId") UUID userId
  ) {
    vote(dealId, userId, -1);
  }

  @Transactional
  private void vote(UUID dealId, UUID userId, int vote) {
    if (userRepository.findById(userId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }

    if (dealRepository.findById(dealId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DEAL_NOT_FOUND");
    }

    // create/update user and deal vote
    UserVoteId userVoteId = new UserVoteId(userId, dealId);
    Optional<UserVoteEntity> userVote = voteRepository.findById(userVoteId);
    userVote.ifPresentOrElse(
        uv -> {
          // remove previous vote from deal
          removePreviousUserVoteFromDeal(dealId, uv.getVote());
          // and update user vote
          uv.setVote(vote);
          voteRepository.save(uv);
          updateDealVote(dealId, vote);
        },
        () -> {
          // add new vote to deal
          updateDealVote(dealId, vote);
          // and add new user vote
          voteRepository.save(new UserVoteEntity(userId, dealId, vote));
        }
    );
  }

  private void removePreviousUserVoteFromDeal(UUID dealId, int vote) {
    dealRepository.findById(dealId).ifPresent(deal -> {
      if (vote == 1) {
        deal.setUpVotes(deal.getUpVotes() - 1);
        dealRepository.save(deal);
      } else if (vote == -1) {
        deal.setDownVotes(deal.getDownVotes() - 1);
        dealRepository.save(deal);
      } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "INVALID_VOTE_VALUE");
      }
    });
  }

  private void updateDealVote(UUID dealId, int vote) {
    dealRepository.findById(dealId).ifPresent(deal -> {
      if (vote == 1) {
        deal.setUpVotes(deal.getUpVotes() + 1);
        dealRepository.save(deal);
      } else if (vote == -1) {
        deal.setDownVotes(deal.getDownVotes() + 1);
        dealRepository.save(deal);
      } else {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "INVALID_VOTE_VALUE");
      }
    });
  }
}
