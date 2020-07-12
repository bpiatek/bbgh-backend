package com.github.bpiatek.bbghbackend.dao;

import com.github.bpiatek.bbghbackend.model.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Integer> {

}