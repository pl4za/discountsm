package com.mysaving.discountsm.deal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DealsController {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private DealRepository dealRepository;

  @GetMapping("deals")
  @ResponseBody
  public List<DealEntity> getAllDeals() {
    String sql = """
        INSERT INTO deal (title, description) VALUES ('title example', 'description example')
        """;

    jdbcTemplate.update(sql);

    return dealRepository.findAll();
  }
}
