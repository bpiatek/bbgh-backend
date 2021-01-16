package com.github.bpiatek.bbghbackend.model.player;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_DOWN;

import com.github.bpiatek.bbghbackend.model.mention.api.MentionResponse;
import com.github.bpiatek.bbghbackend.model.player.api.PlayerNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PlayerFacade {

  private final PlayerRepository playerRepository;

  public Player save(Player player) {
    return playerRepository.save(player);
  }

  public Player findById(Long id) {
    log.debug("Looking for PLAYER with ID: {}", id);
    return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
  }

  public Integer findLastSavedPlayer() {
    log.debug("Find last saved PLAYER ID in database");
    return playerRepository.findLastPlayerIdRead();
  }

  public Page<Player> findAll(Pageable pageable) {
    return playerRepository.findAll(pageable);
  }

  public Page<Player> search(String text, Pageable pageable) {
    log.debug("Searching for player: {}", text);
    return playerRepository.search(text, pageable);
  }

  public BigDecimal playerPercentage(List<MentionResponse> mentions) {
    SentimentCounter sentimentCounter = populateSentimentCounter(mentions);
    if (noMentions(sentimentCounter)) {
      return new BigDecimal("-1");
    }

    if (noNegativeMentions(sentimentCounter)) {
      return new BigDecimal("100");
    }

    BigDecimal positiveAndNegative = sentimentCounter.getPositive().add(sentimentCounter.getNegative());

    // (POSITIVE / (POSITIVE + NEGATIVE) ) * 100
    return sentimentCounter.getPositive()
        .divide(positiveAndNegative, 2, HALF_DOWN)
        .multiply(new BigDecimal("100"));
  }

  private boolean noMentions(SentimentCounter sentimentCounter) {
    return sentimentCounter.getNegative().add(sentimentCounter.positive).compareTo(ZERO) == 0;
  }

  private boolean noNegativeMentions(SentimentCounter sentimentCounter) {
    return sentimentCounter.getNegative().compareTo(ZERO) == 0;
  }

  private SentimentCounter populateSentimentCounter(List<MentionResponse> mentions) {
    SentimentCounter sentimentCounter = new SentimentCounter();
    for (MentionResponse response : mentions) {
      switch (response.getMentionSentiment()) {
        case NEUTRAL:
          sentimentCounter.addNeutral();
          break;
        case NEGATIVE:
          sentimentCounter.addNegative();
          break;
        case POSITIVE:
          sentimentCounter.addPositive();
          break;
        default:
      }
    }

    return sentimentCounter;
  }

  @Data
  private static class SentimentCounter {
    BigDecimal positive = ZERO;
    BigDecimal negative = ZERO;
    BigDecimal neutral = ZERO;

    void addPositive() {
      this.positive = this.positive.add(ONE);
    }

    void addNegative() {
      this.negative = this.negative.add(ONE);
    }

    void addNeutral() {
      this.neutral = this.neutral.add(ONE);
    }
  }
}
