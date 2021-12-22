package com.mysaving.discountsm.deal;

import java.util.List;
import java.util.Optional;
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

  @GetMapping("{dealUid}")
  Optional<DealResponse> getDeal(@PathVariable(value = "dealUid") UUID dealUid);

  @PostMapping
  UUID createDeal(@RequestBody DealRequest dealRequest);

  @PutMapping("{dealUid}/person/{personUid}/up-vote")
  void upvoteDeal(
      @PathVariable(value = "dealUid") UUID dealUid,
      @PathVariable(value = "personUid") UUID personUid
  );

  @PutMapping("{dealUid}/person/{personUid}/down-vote")
  void downVoteDeal(
      @PathVariable(value = "dealUid") UUID dealUid,
      @PathVariable(value = "personUid") UUID personUid
  );
}
