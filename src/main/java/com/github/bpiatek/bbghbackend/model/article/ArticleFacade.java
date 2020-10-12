package com.github.bpiatek.bbghbackend.model.article;

import com.github.bpiatek.bbghbackend.model.article.search.ArticleSearchResult;
import com.github.bpiatek.bbghbackend.model.article.search.ArticleSearcher;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Bartosz Piatek on 26/07/2020
 */
@Service
@AllArgsConstructor
public class ArticleFacade {

  private final ArticleRepository articleRepository;
  private final ArticleSearcher articleSearcher;

  public Article findById(Long id) {
    return articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
  }

  public Page<ArticleSearchResult> search(Pageable pageable, String query) {
    return articleSearcher.find(pageable, query);
  }

  public List<Article> findByUrl(String url) {
    return articleRepository.findByUrl(url);
  }

  public Article save(Article article) {
    return articleRepository.save(article);
  }
}
