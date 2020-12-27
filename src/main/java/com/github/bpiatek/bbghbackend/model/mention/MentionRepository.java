package com.github.bpiatek.bbghbackend.model.mention;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
interface MentionRepository extends Repository<Mention, Long>,
                                    QuerydslPredicateExecutor<Mention>,
                                    QuerydslBinderCustomizer<QMention> {

  @Override
  default void customize(QuerydslBindings bindings, QMention qMention) {
    bindings.bind(qMention.id)
        .all(((path, value) -> Optional.of(path.in(value))));

    bindings.bind(qMention.sentiment)
        .all(((path, value) -> Optional.of(path.in(value))));
  }

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

  @Modifying
  @Query("UPDATE Mention m SET m.sentiment = :sentiment, m.sentimentMarkedByHuman = FALSE WHERE m.id in :ids")
  int setSentimentForMentionsWithIds(@Param("sentiment") MentionSentiment sentiment,
                                     @Param("ids") List<Long> ids);
}
