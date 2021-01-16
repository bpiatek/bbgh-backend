package com.github.bpiatek.bbghbackend.model.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bartosz Piatek on 16/01/2021
 */
@Log4j2
@Service
@Getter
@Setter
@RequiredArgsConstructor
class NamesAndSurnamesProvider {

  private List<String> distinctFirstNames;
  private List<String> distinctLastNames;
  private final PlayerRepository playerRepository;

  @EventListener
  public void afterApplicationStartup(ContextRefreshedEvent event) {
    log.info("Loading names and surnames into cache...");
    distinctLastNames = playerRepository.findDistinctLastNames();
    distinctFirstNames = playerRepository.findDistinctFirstNames();
    log.info("Distinct names: {} and surnames: {} successfully loaded into cache", distinctFirstNames.size(), distinctLastNames.size());
  }
}
