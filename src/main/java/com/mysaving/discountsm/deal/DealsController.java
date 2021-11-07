package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.person.PersonRepository;
import com.mysaving.discountsm.vote.PersonVoteEntity;
import com.mysaving.discountsm.vote.PersonVoteId;
import com.mysaving.discountsm.vote.VoteRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
  private PersonRepository personRepository;

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
      existingEntity.setNewPriceAmount(dealEntity.getNewPriceAmount());
      existingEntity.setNewPriceCurrency(dealEntity.getNewPriceCurrency());
      existingEntity.setOldPriceAmount(dealEntity.getOldPriceAmount());
      existingEntity.setOldPriceCurrency(dealEntity.getOldPriceCurrency());
      existingEntity.setUpVotes(dealEntity.getUpVotes());
      existingEntity.setDownVotes(dealEntity.getDownVotes());
      existingEntity.setPosted(dealEntity.getPosted());
      existingEntity.setExpiry(dealEntity.getExpiry());
      existingEntity.setDealLink(dealEntity.getDealLink());
      existingEntity.setImageLink(dealEntity.getImageLink());
    });
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(method = RequestMethod.GET)
  public List<DealWithPersonVote> getAllDeals() {
    return dealRepository.findAll().stream()
        .map(deal -> new DealWithPersonVote(deal, 0))
        .collect(Collectors.toList());
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/people/{personId}", method = RequestMethod.GET)
  public List<DealWithPersonVote> getAllDealsWithPersonVote(@PathVariable(value = "personId") UUID personId) {
    return dealRepository.findAll().stream()
        .map(deal -> new DealWithPersonVote(
            deal,
            voteRepository.findById(new PersonVoteId(personId, deal.getId())).map(PersonVoteEntity::getVote).orElse(0))
        )
        .collect(Collectors.toList());
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/{dealId}/people/{personId}", method = RequestMethod.GET)
  public Optional<PersonVoteEntity> getVote(
      @PathVariable(value = "dealId") UUID dealId,
      @PathVariable(value = "personId") UUID personId) {
    return voteRepository.findById(new PersonVoteId(personId, dealId));
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/{dealId}/people/{personId}/up-vote", method = RequestMethod.PUT)
  public void upvoteDeal(
      @PathVariable(value = "dealId") UUID dealId,
      @PathVariable(value = "personId") UUID personId
  ) {
    vote(dealId, personId, 1);
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/{dealId}/people/{personId}/down-vote", method = RequestMethod.PUT)
  public void downVoteDeal(
      @PathVariable(value = "dealId") UUID dealId,
      @PathVariable(value = "personId") UUID personId
  ) {
    vote(dealId, personId, -1);
  }

  @Transactional
  private void vote(UUID dealId, UUID personId, int vote) {
    if (personRepository.findById(personId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PERSON_NOT_FOUND");
    }

    if (dealRepository.findById(dealId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DEAL_NOT_FOUND");
    }

    // create/update person and deal vote
    PersonVoteId personVoteId = new PersonVoteId(personId, dealId);
    Optional<PersonVoteEntity> personVote = voteRepository.findById(personVoteId);
    personVote.ifPresentOrElse(
        uv -> {
          // remove previous vote from deal
          removePreviousPersonVoteFromDeal(dealId, uv.getVote());
          // and update person vote
          uv.setVote(vote);
          voteRepository.save(uv);
          updateDealVote(dealId, vote);
        },
        () -> {
          // add new vote to deal
          updateDealVote(dealId, vote);
          // and add new person vote
          voteRepository.save(new PersonVoteEntity(personId, dealId, vote));
        }
    );
  }

  private void removePreviousPersonVoteFromDeal(UUID dealId, int vote) {
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
