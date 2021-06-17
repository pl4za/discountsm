package com.mysaving.discountsm.deal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DealsController {

  @Autowired
  private DealRepository dealRepository;

  @CrossOrigin(origins = "http://localhost:3000")
  @GetMapping("deals")
  @ResponseBody
  public List<DealEntity> getAllDeals() throws InterruptedException {
    Thread.sleep(2000);
    return dealRepository.findAll();
  }
}
