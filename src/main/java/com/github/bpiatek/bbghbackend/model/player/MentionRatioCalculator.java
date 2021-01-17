package com.github.bpiatek.bbghbackend.model.player;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_DOWN;

import com.github.bpiatek.bbghbackend.model.mention.api.SentimentCounter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by Bartosz Piatek on 17/01/2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
class MentionRatioCalculator {

  public SentimentCounter calculate(SentimentCounter sentimentCounter) {
    return playerRatioPercentage(sentimentCounter);
  }

  private SentimentCounter playerRatioPercentage(SentimentCounter sentimentCounter) {
    if (noMentions(sentimentCounter)) {
      return sentimentCounter;
    }

    if (noNegativeMentions(sentimentCounter)) {
      sentimentCounter.setRatio(new BigDecimal("100.00"));
      return  sentimentCounter;
    }

    BigDecimal positiveAndNegative = sentimentCounter.getPositive().add(sentimentCounter.getNegative());

    // (POSITIVE / (POSITIVE + NEGATIVE) ) * 100
    BigDecimal ratio = sentimentCounter.getPositive()
        .divide(positiveAndNegative, 2, HALF_DOWN)
        .multiply(new BigDecimal("100"));

    sentimentCounter.setRatio(ratio);

    return sentimentCounter;
  }

  private boolean noMentions(SentimentCounter sentimentCounter) {
    return sentimentCounter.getNegative().add(sentimentCounter.getPositive()).compareTo(ZERO) == 0;
  }

  private boolean noNegativeMentions(SentimentCounter sentimentCounter) {
    return sentimentCounter.getNegative().compareTo(ZERO) == 0;
  }
}
