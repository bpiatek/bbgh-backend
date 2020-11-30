package com.github.bpiatek.bbghbackend.model.article;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Bartosz Piatek on 26/07/2020
 */
@Service
public class ArticleFacade {

  private final Clock clock;
  private final ArticleRepository articleRepository;
  private Integer daysBack;

  public ArticleFacade(ArticleRepository articleRepository,
                       @Value("${article.daysback}")Integer daysBack,
                       Clock clock) {
    this.articleRepository = articleRepository;
    this.daysBack = daysBack;
    this.clock = clock;
  }

  public Article findById(Long id) {
    return articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
  }

  public Page<Article> findAll(Pageable pageable) {
    return articleRepository.findAll(pageable);
  }

  public List<Article> findByUrl(String url) {
    return articleRepository.findByUrl(url);
  }

  public Article save(Article article) {
    return articleRepository.save(article);
  }

  public List<Article> findArticlesNDaysOld() {
    final LocalDateTime threeDaysAgo = LocalDateTime.now(clock).minusDays(daysBack);
    return articleRepository.findAllWithDateAfter(threeDaysAgo);
  }
}
