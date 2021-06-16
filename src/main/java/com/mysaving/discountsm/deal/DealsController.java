package com.mysaving.discountsm.deal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DealsController {

  @Autowired
  private DealRepository dealRepository;

  @GetMapping("deals")
  @ResponseBody
  public List<DealEntity> getAllDeals() {
    return dealRepository.findAll();
  }
}
