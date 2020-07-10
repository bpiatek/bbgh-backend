package com.github.bpiatek.bbghbackend.ninetyminutes;

import static com.github.bpiatek.bbghbackend.ninetyminutes.NinetyMinutesCrawlerController.NINETY_MINUTES_URL;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Pattern;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Log4j2
class NinetyMinutesCrawler extends WebCrawler {

  private static final Pattern FILTERS = Pattern.compile(
      ".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" +
      "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {
    String href = url.getURL().toLowerCase();
    if (FILTERS.matcher(href).matches()) {
      return false;
    }

    return href.startsWith("http://www.90minut.pl/news/");
  }

  @Override
  public void visit(Page page) {
    if(isMainPage(page)) {
      return;
    }

    System.out.println(page.getWebURL().getURL());
  }

  private boolean isMainPage(Page page) {
    return NINETY_MINUTES_URL.equals(page.getWebURL().getURL());
  }
}
