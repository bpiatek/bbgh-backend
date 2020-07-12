package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import com.github.bpiatek.bbghbackend.model.Article;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.springframework.stereotype.Service;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Service
class ArticleCreator {

  private final NinetyMinutesArticleExtractor articleExtractor;
  private final NinetyMinutesCommentsExtractor commentsExtractor;

  ArticleCreator(NinetyMinutesArticleExtractor articleExtractor,
                 NinetyMinutesCommentsExtractor commentsExtractor) {
    this.articleExtractor = articleExtractor;
    this.commentsExtractor = commentsExtractor;
  }

  Article create(Page page, HtmlParseData htmlParseData) {
    final String html = htmlParseData.getHtml();

    final Article article = Article.builder()
        .url(page.getWebURL().getURL())
        .title(htmlParseData.getTitle())
        .content(articleExtractor.getArticleContentAsText(html))
        .creationDate(articleExtractor.getArticleDateTime(html))
        .build();

    article.setComments(commentsExtractor.getComments(html));

    return article;
  }
}
