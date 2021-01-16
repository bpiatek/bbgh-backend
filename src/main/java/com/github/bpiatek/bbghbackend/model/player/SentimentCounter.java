package com.github.bpiatek.bbghbackend.model.player;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Bartosz Piatek on 16/01/2021
 */
@Data
public class SentimentCounter {
  private BigDecimal positive = ZERO;
  private BigDecimal negative = ZERO;
  private BigDecimal neutral = ZERO;
  private BigDecimal notChecked = ZERO;
  private BigDecimal ratio = ZERO;

  void addPositive() {
    this.positive = this.positive.add(ONE);
  }

  void addNegative() {
    this.negative = this.negative.add(ONE);
  }

  void addNeutral() {
    this.neutral = this.neutral.add(ONE);
  }

  void addNotChecked() {
    this.notChecked = this.notChecked.add(ONE);
  }


}
