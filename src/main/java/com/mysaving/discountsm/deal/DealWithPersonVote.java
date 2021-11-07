package com.mysaving.discountsm.deal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealWithPersonVote {

  private DealEntity dealEntity;

  private int personVote;
}


