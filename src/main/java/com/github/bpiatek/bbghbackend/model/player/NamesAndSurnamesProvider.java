package com.github.bpiatek.bbghbackend.model.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bartosz Piatek on 16/01/2021
 */
@Log4j2
@Service
@Getter
@Setter
@RequiredArgsConstructor
class NamesAndSurnamesProvider {

  private List<String> distinctFullNames;
  private final PlayerRepository playerRepository;

  @EventListener
  public void afterApplicationStartup(ContextRefreshedEvent event) {
    log.info("Loading distinct full names into cache...");
    distinctFullNames = playerRepository.distinctFullNames()
        .stream().map(s -> s.replace(',', ' '))
        .collect(Collectors.toList());

    log.info("Distinct full names: {} successfully loaded into cache",
             distinctFullNames.size());
  }
}
