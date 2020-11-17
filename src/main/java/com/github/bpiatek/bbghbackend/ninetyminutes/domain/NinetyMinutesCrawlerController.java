package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import com.github.bpiatek.bbghbackend.model.article.ArticleFacade;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Log4j2
@Service
class NinetyMinutesCrawlerController {

  public static final String NINETY_MINUTES_URL = "http://www.90minut.pl/";

  private final ArticleCreator articleCreator;
  private final ArticleFacade articleFacade;
  private final String crawlerTempFolder;
  private CrawlController crawlController;

  NinetyMinutesCrawlerController(
      ArticleCreator articleCreator,
      ArticleFacade articleFacade,
      @Value("${crawler.storage.folder}") String crawlerTempFolder
  ) {
    this.articleCreator = articleCreator;
    this.articleFacade = articleFacade;
    this.crawlerTempFolder = crawlerTempFolder;
  }

  void run90minutesCrawler() throws Exception {
    log.info("Running crawler for portal 90minut.pl.");
    CrawlConfig config = new CrawlConfig();
    config.setCrawlStorageFolder(crawlerTempFolder + "90minut");
    config.setPolitenessDelay(1000);
    final PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    crawlController = new CrawlController(config, pageFetcher, robotstxtServer);
    crawlController.addSeed(NINETY_MINUTES_URL);

    int numberOfCrawlers = 1;

    CrawlController.WebCrawlerFactory<NinetyMinutesCrawler> factory = () -> new NinetyMinutesCrawler(articleCreator, articleFacade);

    crawlController.startNonBlocking(factory, numberOfCrawlers);
  }

  void stop90minutesCrawler() {
    log.info("Stopping crawler for portal 90minut.pl.");
    if (crawlController != null) {
      crawlController.shutdown();
      crawlController.waitUntilFinish();
    }
  }
}
