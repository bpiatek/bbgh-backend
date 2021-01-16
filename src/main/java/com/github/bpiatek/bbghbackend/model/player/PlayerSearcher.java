package com.github.bpiatek.bbghbackend.model.player;

import static java.util.List.of;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bartosz Piatek on 18/11/2020
 */
@Log4j2
@Service
@RequiredArgsConstructor
class PlayerSearcher {

  private final NamesAndSurnamesProvider dataProvider;

  public PlayerSearchResult search(String text) {
    Optional<String> firstName = getFirstName(text);
    Optional<String> lastName = getLastName(text);

    return new PlayerSearchResult(firstName, lastName);
  }

  private Optional<String> getFirstName(String text) {
    return searchForNameOrSurname(text, dataProvider.getDistinctFirstNames());
  }

  private Optional<String> getLastName(String text) {
    return searchForNameOrSurname(text, dataProvider.getDistinctLastNames());
  }

  private Optional<String> searchForNameOrSurname(String text, List<String> distinctNamesOrSurnames) {
    List<String> strings = of(text.trim().split(" "));
    List<String> names = new ArrayList<>();

    for (String name : strings) {
      for (String s : distinctNamesOrSurnames) {
        if(s != null && s.equalsIgnoreCase(name)) {
            names.add(name);
        }
      }
    }

    if(names.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(names.get(0));
  }
}
