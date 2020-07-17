package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import com.github.bpiatek.bbghbackend.model.ArticleHtmlExtractor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Service
class NinetyMinutesArticleExtractor implements ArticleHtmlExtractor {

  private final TextToLocalDateTimeParser localDateTimeParser;

  NinetyMinutesArticleExtractor(TextToLocalDateTimeParser localDateTimeParser) {
    this.localDateTimeParser = localDateTimeParser;
  }

  @Override
  public String getArticleContentAsText(String html) {
    return getArticleContent(html).text();
  }

  @Override
  public String getArticleContentAsHtml(String html) {
    return getArticleContent(html).html();
  }

  @Override
  public LocalDateTime getArticleDateTime(String html) {
    final Document document = Jsoup.parse(html);
    final Node date = document.select("p").get(1).childNode(1);

    return localDateTimeParser.parse(date.toString());
  }

  private static Element getArticleContent(String html) {
    final Document document = Jsoup.parse(html); 
    final Elements blockquote = document.select("blockquote");
    return blockquote.select("tbody").get(0).select("td").get(0);
  }
}
