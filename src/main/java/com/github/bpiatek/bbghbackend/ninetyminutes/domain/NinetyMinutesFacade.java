package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import org.springframework.stereotype.Service;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Service
public class NinetyMinutesFacade {

  private final NinetyMinutesCrawlerController crawlerController;

  public NinetyMinutesFacade(NinetyMinutesCrawlerController crawlerController) {
    this.crawlerController = crawlerController;
  }

  public void runCrawler() throws Exception {
    crawlerController.run90minutesCrawler();
  }

  public void stopCrawler() {
    crawlerController.stop90minutesCrawler();
  }
}
