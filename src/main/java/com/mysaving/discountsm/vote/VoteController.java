package com.mysaving.discountsm.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("vote")
public class VoteController {

  @Autowired
  private VoteRepository voteRepository;


}
