package com.github.bpiatek.bbghbackend.model.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bartosz Piatek on 26/07/2020
 */
@Service
public class ArticleFacade {

  private final ArticleRepository articleRepository;

  public ArticleFacade(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
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
}
