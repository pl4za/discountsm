package com.mysaving.discountsm.deal;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/v1/deal")
public interface DealResource {

  @GetMapping
  List<DealResponse> getAllDeals();

  @PostMapping
  UUID createDeal(@RequestBody DealRequest dealRequest);

  @PutMapping("{dealUid}/up-vote")
  void upvoteDeal(@PathVariable(value = "dealUid") UUID dealUid);

  @PutMapping("{dealUid}/down-vote")
  void downVoteDeal(@PathVariable(value = "dealUid") UUID dealUid);
}
