package com.github.bpiatek.bbghbackend.model.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
interface CommentRepository extends Repository<Comment, Long> {
  Page<Comment> findByArticleId(Long articleId, Pageable pageable);
  Optional<Comment> findById(Long id);
  @Modifying
  @Query("UPDATE Comment c SET c.commentOpinionStatus = :status WHERE c.id = :id")
  int setCommentOpinionStatusById(@Param("id") Long id, @Param("status") CommentOpinionStatus status);
}
