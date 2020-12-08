package com.github.bpiatek.bbghbackend.model.mention;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
interface MentionRepository extends Repository<Mention, Long> {
  Mention save(Mention player);

  Page<Mention> findByCommentId(Long commentId, Pageable pageable);

  Optional<Mention> findById(Long id);

  Page<Mention> findAll(Pageable pageable);

  Page<Mention> findBySentimentIn(Pageable pageable, List<MentionSentiment> sentiments);

  Page<Mention> findByPlayerIdIn(Pageable pageable, List<Long> ids);

  @Modifying
  @Query("UPDATE Mention m SET m.sentiment = :sentiment, m.sentimentMarkedByHuman = :isHuman WHERE m.id = :id")
  int setMentionSentimentById(@Param("id") Long id,
                              @Param("sentiment") MentionSentiment sentiment,
                              @Param("isHuman") boolean isHuman);

}
