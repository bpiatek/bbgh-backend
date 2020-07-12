package com.github.bpiatek.bbghbackend.dao;

import com.github.bpiatek.bbghbackend.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
public interface ArticleRepository extends Repository<Article, Long> {
  Article save(Article article);
  Page<Article> findAll(Pageable pageable);
}
