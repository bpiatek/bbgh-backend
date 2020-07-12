package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import com.github.bpiatek.bbghbackend.model.Article;
import com.github.bpiatek.bbghbackend.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Service
public class NinetyMinutesFacade {

  private final NinetyMinutesCrawlerController crawlerController;
  private final NinetyMinutesArticleRepository articleRepository;
  private final NinetyMinutesCommentRepository commentRepository;

  public NinetyMinutesFacade(
      NinetyMinutesCrawlerController crawlerController,
      NinetyMinutesArticleRepository articleRepository,
      NinetyMinutesCommentRepository commentRepository
  ) {
    this.crawlerController = crawlerController;
    this.articleRepository = articleRepository;
    this.commentRepository = commentRepository;
  }

  public void runCrawler() throws Exception {
    crawlerController.run90minutesCrawler();
  }

  @Transactional
  public Page<Article> findAllArticles(Pageable pageable) {
    return articleRepository.findAll(pageable);
  }

  public Page<Comment> findCommentsByArticleId(Long articleId, Pageable pageable) {
    return commentRepository.findByArticleId(articleId, pageable);
  }
}
