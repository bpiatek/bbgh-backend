package com.github.bpiatek.bbghbackend.model.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by Bartosz Piatek on 16/01/2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SentimentCounter {
  private BigDecimal positive;
  private BigDecimal negative;
  private BigDecimal neutral;
  private BigDecimal notChecked;
  private BigDecimal ratio;
}
