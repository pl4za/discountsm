package com.mysaving.discountsm.deal;

import com.mysaving.discountsm.repository.DealRepository;
import com.mysaving.discountsm.support.Money;
import com.neovisionaries.i18n.CurrencyCode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DealService implements DealResource {

  @Autowired
  private DealRepository dealRepository;

  @Override
  public List<DealResponse> getAllDeals() {
    return dealRepository.findAll().stream()
        .map(deal -> new DealResponse(
            deal.getId(),
            deal.getTitle(),
            deal.getDescription(),
            Money.of(CurrencyCode.valueOf(deal.getNewPriceCurrency()), deal.getNewPriceAmount()),
            Money.of(CurrencyCode.valueOf(deal.getOldPriceCurrency()), deal.getOldPriceAmount()),
            deal.getUpVotes(),
            deal.getDownVotes(),
            deal.getPosted(),
            deal.getExpiry(),
            deal.getDealLink(),
            deal.getImageLink()
        )).toList();
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
  @Transactional
  public void upvoteDeal(UUID dealUid) {
    dealRepository.upVoteDeal(dealUid);
  }

  @Override
  @Transactional
  public void downVoteDeal(UUID dealUid) {
    dealRepository.downVoteDeal(dealUid);
  }
}
