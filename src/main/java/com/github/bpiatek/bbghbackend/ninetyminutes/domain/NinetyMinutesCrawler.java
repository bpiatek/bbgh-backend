package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.domain.NinetyMinutesCrawlerController.NINETY_MINUTES_URL;

import com.github.bpiatek.bbghbackend.dao.ArticleRepository;
import com.github.bpiatek.bbghbackend.model.Article;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Pattern;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Log4j2
class NinetyMinutesCrawler extends WebCrawler {
  private static final String NEWS_URL_TEMPLATE = "http://www.90minut.pl/news/";

  private final ArticleCreator articleCreator;
  private final ArticleRepository articleRepository;

  NinetyMinutesCrawler(
      ArticleCreator articleCreator,
      ArticleRepository articleRepository
  ) {
    this.articleCreator = articleCreator;
    this.articleRepository = articleRepository;
  }

  private static final Pattern FILTERS = Pattern.compile(
      ".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" +
      "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    String href = url.getURL().toLowerCase();
    return !FILTERS.matcher(href).matches() && href.startsWith(NINETY_MINUTES_URL);
  } 

  @Override
  public void visit(Page page) {
    final String url = page.getWebURL().getURL();
    if (isNotNews(url)) {
      log.info("Page URL: {} for portal {} dismissed", url, NINETY_MINUTES_URL);
      return;
    }

    log.debug("Page visited: {}", url);
    HtmlParseData parseData = (HtmlParseData) page.getParseData();
    final Article article = articleCreator.create(page, parseData);

    final Article savedArticle = articleRepository.save(article);
    log.info("Article with ID: {} form portal {} saved", savedArticle.getId(), NINETY_MINUTES_URL);
  }

  private boolean isNotNews(String url) {
    return NINETY_MINUTES_URL.equals(url) || !url.startsWith(NEWS_URL_TEMPLATE) ;
  }
}
