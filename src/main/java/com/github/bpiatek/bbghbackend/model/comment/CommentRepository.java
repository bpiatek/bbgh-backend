package com.github.bpiatek.bbghbackend.model.comment;

import com.github.bpiatek.bbghbackend.model.article.Article;
import com.github.bpiatek.bbghbackend.model.mention.Mention;
import com.github.bpiatek.bbghbackend.model.mention.QMention;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
interface CommentRepository extends Repository<Comment, Long> {
  Page<Comment> findByArticleId(Long articleId, Pageable pageable);

  Optional<Comment> findById(Long id);

  Page<Comment> findAll(Pageable pageable);
}
