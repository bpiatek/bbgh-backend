package com.github.bpiatek.bbghbackend.model.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
interface ArticleRepository extends Repository<Article, Long> {

  Article save(Article article);

  Page<Article> findAll(Pageable pageable);

  Page<Article> findAllByUpdatedAtAfter(Pageable pageable, LocalDateTime after);

  Page<Article> findAllByCreationDateAfter(Pageable pageable, LocalDateTime after);

  List<Article> findByUrl(String url);

  Optional<Article> findById(Long id);

  @Query("SELECT a FROM Article a WHERE a.creationDate >= :daysAgo")
  List<Article> findAllWithDateAfter(@Param("daysAgo") LocalDateTime daysAgo);

  @Query(value = "SELECT "
                 + "distinct a "
                 + "FROM Article a "
                 + "join Comment c"
                 + "    on a.id = c.article.id"
                 + "    join Mention m"
                 + "    on m.comment.id = c.id"
                 + "    join Player p"
                 + "    on p.id = m.player.id "
                 + "where p.id = :playerId ")
  Page<Article> findAllByPlayerId(@Param("playerId") Long playerId, Pageable pageable);
}
