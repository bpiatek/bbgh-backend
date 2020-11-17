package com.github.bpiatek.bbghbackend.scheduler;

import com.github.bpiatek.bbghbackend.ninetyminutes.domain.NinetyMinutesFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Bartosz Piatek on 12/07/2020
 */
@Slf4j
@Component
class CrawlersScheduler {

  private final NinetyMinutesFacade ninetyMinutesFacade;

  CrawlersScheduler(NinetyMinutesFacade ninetyMinutesFacade) {
    this.ninetyMinutesFacade = ninetyMinutesFacade;
  }

  // everyday at 23:59
  @Scheduled(cron = "0 59 23 * * ?")
  public void run90MinutesCrawler() throws Exception {
    log.info("90minutes.pl crawler started on schedule");
    ninetyMinutesFacade.runCrawler();
  }

}
