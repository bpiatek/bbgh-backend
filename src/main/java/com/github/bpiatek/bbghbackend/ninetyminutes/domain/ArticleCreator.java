package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import com.github.bpiatek.bbghbackend.model.article.Article;
import com.github.bpiatek.bbghbackend.model.comment.Comment;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Service
@AllArgsConstructor
class ArticleCreator {

  private final NinetyMinutesArticleExtractor articleExtractor;
  private final NinetyMinutesCommentsExtractor commentsExtractor;

  Article createFromPage(Page page, HtmlParseData htmlParseData) {
    String html = htmlParseData.getHtml();
    LocalDateTime articleCreationDate = articleExtractor.getArticleDateTime(html);

    Article article = Article.builder()
        .url(page.getWebURL().getURL())
        .title(htmlParseData.getTitle())
        .content(articleExtractor.getArticleContentAsText(html))
        .creationDate(articleCreationDate)
        /* TODO
         kiedy zostanie zaimplementowane pobieranie nowych komentarzy do istniejącego artykułu
         popraw tą funkcjonalność
        */
        .updatedAt(articleCreationDate)
        .build();

    List<Comment> comments = commentsExtractor.getComments(html);
    comments.forEach(comment -> comment.setArticle(article));

    article.setComments(comments);

    return article;
  }
}
