package com.github.bpiatek.bbghbackend.model.player;

import lombok.RequiredArgsConstructor;
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
@Service
@RequiredArgsConstructor
class PlayerSearcher {

  private final PlayerRepository playerRepository;

  public Page<Player> search(String text, Pageable pageable) {
    Optional<String> firstName = getFirstName(text);
    Optional<String> lastName = getLastName(text);

    return searchForPlayers(firstName, lastName, pageable);
  }

  private Optional<String> getFirstName(String text) {
    List<String> distinctFirstNames = playerRepository.findDistinctFirstNames();

    return distinctFirstNames.stream()
        .filter(text::contains)
        .findFirst();
  }

  private Optional<String> getLastName(String text) {
    List<String> distinctLastNames = playerRepository.findDistinctLastNames();

    return distinctLastNames.stream()
        .filter(text::contains)
        .findFirst();
  }

  private Page<Player> searchForPlayers(Optional<String> firstName, Optional<String> lastName, Pageable pageable) {
    if (firstAndLastNameIsPresent(firstName, lastName)) {
      return playerRepository.findAllByFirstNameAndLastName(firstName.get(), lastName.get(), pageable);
    } else if (onlyFirstNameIsPresent(firstName, lastName)) {
      return playerRepository.findAllByFirstName(firstName.get(), pageable);
    } else if (onlyLastNameIsPresent(firstName, lastName)) {
      return playerRepository.findAllByLastName(lastName.get(), pageable);
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
