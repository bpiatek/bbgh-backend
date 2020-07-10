package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.domain.NinetyMinutesCrawlerController.NINETY_MINUTES_URL;

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

  private final ArticleCreator articleCreator;

  NinetyMinutesCrawler(ArticleCreator articleCreator) {
    this.articleCreator = articleCreator;
  }

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
    if (isMainPage(page)) {
      return;
    }

    HtmlParseData parseData = (HtmlParseData) page.getParseData();
    final Article article = articleCreator.create(page, parseData);
    prettyPrintArticle(article);
  }

  private void prettyPrintArticle(Article article) {
    System.out.println("****************************");
    System.out.println("URL: " + article.getUrl());
    System.out.println("TYTUŁ: " + article.getTitle());
    System.out.println("DODANIA: " + article.getCreationDate());
    System.out.println("TREŚC: " + article.getContent());

    System.out.println(("KOMENTARZE: "));
    article.getComments().forEach(c -> System.out.println("autor: " + c.getAuthor() + "\nkomentarz: " + c.getContent() + "\n"));

    System.out.println("\n\n\n");
  }

  private boolean isMainPage(Page page) {
    return NINETY_MINUTES_URL.equals(page.getWebURL().getURL());
  }
}
