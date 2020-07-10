package com.github.bpiatek.bbghbackend.ninetyminutes;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Service
public class NinetyMinutesCrawlerController {
  public static final String NINETY_MINUTES_URL = "http://www.90minut.pl/";

  @Value("${crawler.storage.folder}")
  private String crawlerTempFolder;

  public void run90minutesCrawler() throws Exception {
    CrawlConfig config = new CrawlConfig();
    config.setCrawlStorageFolder(crawlerTempFolder + "90minut");
    config.setPolitenessDelay(1000);
    final PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
    controller.addSeed(NINETY_MINUTES_URL);

    int numberOfCrawlers = 2;

    CrawlController.WebCrawlerFactory<NinetyMinutesCrawler> factory = NinetyMinutesCrawler::new;

    controller.start(factory, numberOfCrawlers);
  }
}
