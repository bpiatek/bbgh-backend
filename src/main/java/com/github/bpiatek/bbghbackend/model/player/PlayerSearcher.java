package com.github.bpiatek.bbghbackend.model.player;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bartosz Piatek on 18/11/2020
 */
@Log4j2
@Service
class PlayerSearcher {

  private final PlayerRepository playerRepository;
  private List<String> distinctFirstNames;
  private List<String> distinctLastNames;

  public PlayerSearcher(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  @EventListener
  public void afterApplicationStartup(ContextRefreshedEvent event) {
    log.info("Loading names and surnames into cache...");
    distinctLastNames = playerRepository.findDistinctLastNames();
    distinctFirstNames = playerRepository.findDistinctFirstNames();
    log.info("Names and surnames successfully loaded into cache");
  }

  public Page<Player> search(String text, Pageable pageable) {
    Optional<String> firstName = getFirstName(text);
    Optional<String> lastName = getLastName(text);

    return searchForPlayers(firstName, lastName, pageable);
  }

  private Optional<String> getFirstName(String text) {
    return distinctFirstNames.stream()
        .filter(firstName -> containsIgnoreCase(text, firstName))
        .findFirst();
  }

  private Optional<String> getLastName(String text) {
    return distinctLastNames.stream()
        .filter(lastName -> containsIgnoreCase(text, lastName))
        .findFirst();
  }

  private Page<Player> searchForPlayers(Optional<String> firstName, Optional<String> lastName, Pageable pageable) {
    if (firstAndLastNameIsPresent(firstName, lastName)) {
      log.info("Searching for PLAYER with firstName: {} and lastName: {}", firstName, lastName);
      return playerRepository.findAllByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName.get(), lastName.get(), pageable);
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
}
