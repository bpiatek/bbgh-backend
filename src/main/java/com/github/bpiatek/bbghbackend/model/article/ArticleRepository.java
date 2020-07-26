package com.github.bpiatek.bbghbackend.model.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
interface ArticleRepository extends Repository<Article, Long> {
  Article save(Article article);
  Page<Article> findAll(Pageable pageable);
  List<Article> findByUrl(String url);
  Optional<Article> findById(Long id);
}
