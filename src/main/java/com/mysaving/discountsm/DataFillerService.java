package com.mysaving.discountsm;

import com.mysaving.discountsm.deal.Deal;
import com.mysaving.discountsm.deal.DealRepository;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataFillerService {

  @Autowired
  DealRepository dealRepository;

  @PostConstruct
  @Transactional
  public void fillData() {
    Deal deal = new Deal(
        "title example",
        "description example"
    );

    dealRepository.save(deal);
  }
}
