package com.mysaving.discountsm.repository;

import com.mysaving.discountsm.deal.DealEntity;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DealRepository extends JpaRepository<DealEntity, UUID> {

  @Modifying
  @Query(value = """
      INSERT INTO deal (
        id,
        title,
        description,
        new_price_amount,
        new_price_currency,
        old_price_amount,
        old_price_currency,
        score,
        posted,
        deal_link,
        image_link
      )
      VALUES (
        :id,
        :title,
        :description,
        :newPriceAmount,
        :newPriceCurrency,
        :oldPriceAmount,
        :oldPriceCurrency,
        :score,
        :posted,
        :dealLink,
        :imageLink
      )
      """, nativeQuery = true)
  void createDeal(
      @Param("id") UUID id,
      @Param("title") String title,
      @Param("description") String description,
      @Param("newPriceAmount") BigDecimal newPriceAmount,
      @Param("newPriceCurrency") String newPriceCurrency,
      @Param("oldPriceAmount") BigDecimal oldPriceAmount,
      @Param("oldPriceCurrency") String oldPriceCurrency,
      @Param("score") int upVotes,
      @Param("posted") Instant posted,
      @Param("dealLink") String dealLink,
      @Param("imageLink") String imageLink
  );

  @Query(value = """
      UPDATE deal SET score = score + 1 WHERE id = :id
      RETURNING score
      """, nativeQuery = true)
  int likeDeal(@Param("id") UUID id);
}
