package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Service
@AllArgsConstructor
public class NinetyMinutesFacade {

  private final NinetyMinutesCrawlerController crawlerController;
  private final NinetyMinutesPlayerCrawler playerCrawler;

  public void runCrawler() throws Exception {
    crawlerController.run90minutesCrawler();
  }

  public void stopCrawler() {
    crawlerController.stop90minutesCrawler();
  }

  public void runPlayersCrawler() {
    playerCrawler.startCrawlingForPlayers();
  }
}
