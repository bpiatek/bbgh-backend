package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.domain.NinetyMinutesCrawlerController.NINETY_MINUTES_URL;

import com.github.bpiatek.bbghbackend.model.article.Article;
import com.github.bpiatek.bbghbackend.model.comment.Comment;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Log4j2
@Service
@AllArgsConstructor
public class ArticleCreator {

  private final NinetyMinutesArticleExtractor articleExtractor;
  private final NinetyMinutesCommentsExtractor commentsExtractor;
  private final Clock clock;

  public Article createFromPage(Page page, HtmlParseData htmlParseData, Optional<Article> article) {
    String html = htmlParseData.getHtml();

    return article
        .map(art -> updateExistingArticle(art, html))
        .orElseGet(() -> createNewArticle(page, htmlParseData, html));
  }

  private Article createNewArticle(Page page, HtmlParseData htmlParseData, String html) {
    log.info("Extracting new ARTICLE from portal {}", NINETY_MINUTES_URL);
    LocalDateTime articleCreationDate = articleExtractor.getArticleDateTime(html);

    Article article = Article.builder()
        .url(page.getWebURL().getURL())
        .title(htmlParseData.getTitle())
        .content(articleExtractor.getArticleContentAsText(html))
        .creationDate(articleCreationDate)
        .build();

    List<Comment> comments = commentsExtractor.getComments(html);
    comments.forEach(comment -> comment.setArticle(article));

    article.setComments(comments);

    return article;
  }

  private Article updateExistingArticle(Article article, String html) {
    log.info("Searching for new comments for ARTICLE with ID: {} extracted at: {}",
             article.getId(),
             article.getCreationDate());

    List<Comment> comments = commentsExtractor.getComments(html);

    return addNewCommentsToArticle(comments, article);
  }

  private Article addNewCommentsToArticle(List<Comment> commentsFromArticle, Article article) {
    if(articleHasComments(commentsFromArticle)) {
      article.setUpdatedAt(LocalDateTime.now(clock));

      commentsFromArticle.forEach(comment -> {
        if(foundNewComment(article, comment)) {
          log.info("Adding new comment created at: {} to existing ARTICLE with ID: {}",
                   comment.getDateAdded(),
                   article.getId());

          comment.setArticle(article);
          article.addComment(comment);
        }
      });
    } else {
      log.info("There are no new comments for ARTICLE with ID: {}", article.getId());
    }

    return article;
  }

  private boolean articleHasComments(List<Comment> commentsFromArticle) {
    return !commentsFromArticle.isEmpty();
  }

  private boolean foundNewComment(Article article, Comment comment) {
    for (Comment c: article.getComments()) {
      if(isTheSameComment(comment, c)) {
        return false;
      }
    }

    return true;
  }

  private boolean isTheSameComment(Comment comment, Comment c) {
    return c.getAuthor().equals(comment.getAuthor())
           && c.getContent().equals(comment.getContent())
           && c.getDateAdded().equals(comment.getDateAdded());
  }
}
