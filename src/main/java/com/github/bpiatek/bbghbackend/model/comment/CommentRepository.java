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

  Page<Comment> findAll(Pageable pageable);

  @Query(nativeQuery = true,
      value = "" +
        "SELECT DISTINCT comment.* " +
        "FROM comment JOIN mention m ON comment.id = m.comment_id " +
        "WHERE sentiment_marked_by_human = 1 " +
        "AND sentiment = 'NEGATIVE'",
      countQuery = "" +
        "SELECT COUNT(DISTINCT comment.id)" +
        "FROM comment JOIN mention m ON comment.id = m.comment_id " +
        "WHERE sentiment_marked_by_human = 1 " +
        "AND sentiment = 'NEGATIVE'"
  )
  Page<Comment> findWithNegativeMentionsMarkedByHuman(Pageable pageable);

  @Modifying
  @Query("UPDATE Comment m SET m.isHateSpeech = :isHateSpeech WHERE m.id = :id")
  int setMentionSentimentById(@Param("id") Long id, @Param("isHateSpeech") Boolean isHateSpeech);
}
