package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import com.github.bpiatek.bbghbackend.model.article.Article;
import com.github.bpiatek.bbghbackend.model.comment.Comment;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.springframework.stereotype.Service;

import java.util.List;

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

  Article createFromPage(Page page, HtmlParseData htmlParseData) {
    final String html = htmlParseData.getHtml();

    final Article article = Article.builder()
        .url(page.getWebURL().getURL())
        .title(htmlParseData.getTitle())
        .content(articleExtractor.getArticleContentAsText(html))
        .creationDate(articleExtractor.getArticleDateTime(html))
        .build();

    final List<Comment> comments = commentsExtractor.getComments(html);
    article.setComments(comments);

    return article;
  }
}
