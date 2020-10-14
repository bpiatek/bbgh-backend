package com.github.bpiatek.bbghbackend.model.mention;

import com.github.bpiatek.bbghbackend.model.article.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
public interface MentionRepository extends Repository<Mention, Long> {
  Mention save(Mention player);

  Optional<Mention> findById(Long id);

  Page<Mention> findAll(Pageable pageable);

}