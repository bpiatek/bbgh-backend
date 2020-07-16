package com.github.bpiatek.bbghbackend.dao;

import com.github.bpiatek.bbghbackend.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
public interface CommentRepository extends Repository<Comment, Long> {
  Page<Comment> findByArticleId(Long articleId, Pageable pageable);
}
