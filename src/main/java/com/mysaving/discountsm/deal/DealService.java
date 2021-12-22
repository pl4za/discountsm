package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.repository.DealRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DealService implements DealResource{

  @Autowired
  private DealRepository dealRepository;

  @Override
  public List<DealResponse> getAllDeals() {
    return null;
  }

  @Override
  public Optional<DealResponse> getDeal(UUID dealUid) {
    return Optional.empty();
  }

  @Override
  @Transactional
  public UUID createDeal(DealRequest dealRequest) {
    UUID dealUid = UUID.randomUUID();
    dealRepository.createDeal(
        dealUid,
        dealRequest.title(),
        dealRequest.description(),
        dealRequest.newPriceMoney().amount(),
        dealRequest.newPriceMoney().currencyCode().toString(),
        dealRequest.oldPriceMoney().amount(),
        dealRequest.oldPriceMoney().currencyCode().toString(),
        0,
        0,
        Instant.now(),
        dealRequest.expiry(),
        dealRequest.dealLink(),
        dealRequest.imageLink()
    );

    return dealUid;
  }

  @Override
  public void upvoteDeal(UUID dealUid, UUID personUid) {

  }

  @Override
  public void downVoteDeal(UUID dealUid, UUID personUid) {

  }
}
