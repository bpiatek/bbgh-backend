package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.domain.NinetyMinutesCrawlerController.NINETY_MINUTES_URL;

import com.github.bpiatek.bbghbackend.model.article.Article;
import com.github.bpiatek.bbghbackend.model.article.ArticleFacade;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Pattern;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Log4j2
@AllArgsConstructor
class NinetyMinutesCrawler extends WebCrawler {

  private static final String NEWS_URL_TEMPLATE = "http://www.90minut.pl/news/";
  private static final String NEWS_LIST_URL_TEMPLATE = "http://www.90minut.pl/news.php?"; // www.90minut.pl is another great page written in PHP.

  private final ArticleCreator articleCreator;
  private final ArticleFacade articleFacade;

  private static final Pattern FILTERS = Pattern.compile(
      ".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" +
      "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    String href = url.getURL().toLowerCase();
    return !FILTERS.matcher(href).matches() && (isNewsDetailsUrl(href) || isNewsListUrl(href))
                      && articleFacade.shouldCheckForNewComments(href);
  }

  @Override
  public void visit(Page page) {
    final String url = page.getWebURL().getURL();
    if (isNewsListUrl(url)) {
      log.debug("News list visited: {}", url);
    }

    if (isNewsDetailsUrl(url)) {
      HtmlParseData parseData = (HtmlParseData) page.getParseData();

      Article savedArticle = articleFacade.createAndSave(url, page, parseData);
      log.info("Article with ID: {} from portal {} saved", savedArticle.getId(), NINETY_MINUTES_URL);
    }

    log.debug("Page dismissed: {}", url);
  }

  private boolean isNewsDetailsUrl(String url) {
    return url.startsWith(NEWS_URL_TEMPLATE);
  }

  private boolean isNewsListUrl(String url) {
    return url.startsWith(NEWS_LIST_URL_TEMPLATE);
  }
}
