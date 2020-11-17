package com.github.bpiatek.bbghbackend.model.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
interface CommentRepository extends Repository<Comment, Long> {
  Page<Comment> findByArticleId(Long articleId, Pageable pageable);
  Optional<Comment> findById(Long id);
}
