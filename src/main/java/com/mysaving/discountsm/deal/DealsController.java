package com.mysaving.discountsm.deal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("deals")
public class DealsController {

  @Autowired
  private DealRepository dealRepository;

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(method = RequestMethod.GET)
  public List<DealEntity> getAllDeals() throws InterruptedException {
    // TODO: remove sleep
    Thread.sleep(2000);
    return dealRepository.findAll();
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public Optional<DealEntity> getDeal(@PathVariable(value = "id") UUID id) {
    return dealRepository.findById(id);
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @Transactional
  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public void updateDeal(@PathVariable(value = "id") UUID id, @RequestBody DealEntity dealEntity) {
    dealRepository.findById(id).ifPresent(existingEntity -> {
      existingEntity.setTitle(dealEntity.getTitle());
      existingEntity.setDescription(dealEntity.getDescription());
      existingEntity.setNewPrice(dealEntity.getNewPrice());
      existingEntity.setOldPrice(dealEntity.getOldPrice());
      existingEntity.setUpVotes(dealEntity.getUpVotes());
      existingEntity.setDownVotes(dealEntity.getDownVotes());
      existingEntity.setPosted(dealEntity.getPosted());
      existingEntity.setExpiry(dealEntity.getExpiry());
      existingEntity.setLink(dealEntity.getLink());
      existingEntity.setImage(dealEntity.getImage());
    });
  }

  @CrossOrigin(origins = "http://localhost:3000")
  @RequestMapping(method = RequestMethod.POST)
  public void createDeal(@RequestBody DealEntity dealEntity) {
    dealRepository.save(dealEntity);
  }
}
