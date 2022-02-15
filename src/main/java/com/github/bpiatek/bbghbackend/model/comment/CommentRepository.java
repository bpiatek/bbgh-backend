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

  @Query(value = "SELECT * FROM comment c INNER JOIN article a ON a.id = c.article_id WHERE a.title LIKE '%Lech%' AND MOD(a.id, 25) = 0 AND a.id < 14000", nativeQuery = true)
  Page<Comment> findAll(Pageable pageable);

  @Modifying
  @Query("UPDATE Comment m SET m.isHateSpeech = :isHateSpeech WHERE m.id = :id")
  int setMentionSentimentById(@Param("id") Long id, @Param("isHateSpeech") Boolean isHateSpeech);
}
