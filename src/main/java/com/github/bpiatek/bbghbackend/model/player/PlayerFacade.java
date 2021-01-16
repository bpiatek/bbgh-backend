package com.github.bpiatek.bbghbackend.model.player;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

import com.github.bpiatek.bbghbackend.model.mention.api.MentionResponse;
import com.github.bpiatek.bbghbackend.model.player.api.PlayerNotFoundException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PlayerFacade {

  private final PlayerRepository playerRepository;
  private final PlayerSearcher playerSearcher;

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
    final PlayerSearchResult search = playerSearcher.search(text);
    return searchForPlayers(search.getFirstName(), search.getLastName(), pageable);
  }

  public BigDecimal playerPercentage(List<MentionResponse> mentions) {
    SentimentCounter sentimentCounter = countPercentage(mentions);
    if (sentimentCounter.getNegative().compareTo(ZERO) == 0) {
      return new BigDecimal("100");
    }

    BigDecimal add = sentimentCounter.getPositive().add(sentimentCounter.getNegative());

    return sentimentCounter.getPositive().divide(add, 2, RoundingMode.HALF_DOWN).multiply(new BigDecimal("100"));
  }

  private SentimentCounter countPercentage(List<MentionResponse> mentions) {
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

  private Page<Player> searchForPlayers(Optional<String> firstName, Optional<String> lastName, Pageable pageable) {
    if (firstAndLastNameIsPresent(firstName, lastName)) {
      log.info("Searching for PLAYER with firstName: {} and lastName: {}", firstName, lastName);
      return playerRepository.findAllByFirstNameIgnoreCaseAndLastNameIgnoreCase(
          firstName.get(),
          lastName.get(),
          pageable
      );
    } else if (onlyFirstNameIsPresent(firstName, lastName)) {
      log.info("Searching for PLAYER with firstName: {}", lastName);
      return playerRepository.findAllByFirstNameIgnoreCase(firstName.get(), pageable);
    } else if (onlyLastNameIsPresent(firstName, lastName)) {
      log.info("Searching for PLAYER with lastName: {}", lastName);
      return playerRepository.findAllByLastNameIgnoreCase(lastName.get(), pageable);
    } else {
      return new PageImpl<>(new ArrayList<>());
    }
  }

  private boolean onlyFirstNameIsPresent(Optional<String> firstName, Optional<String> lastName) {
    return lastName.isEmpty() && firstName.isPresent();
  }

  private boolean onlyLastNameIsPresent(Optional<String> firstName, Optional<String> lastName) {
    return firstName.isEmpty() && lastName.isPresent();
  }

  private boolean firstAndLastNameIsPresent(Optional<String> firstName, Optional<String> lastName) {
    return firstName.isPresent() && lastName.isPresent();
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
