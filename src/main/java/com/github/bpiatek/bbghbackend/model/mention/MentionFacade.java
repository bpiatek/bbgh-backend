package com.github.bpiatek.bbghbackend.model.mention;

import com.github.bpiatek.bbghbackend.model.article.search.ArticleSearchResult;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Service
@AllArgsConstructor
public class MentionFacade {
  private final MentionRepository mentionRepository;

  public Mention save(Mention player) {
    return mentionRepository.save(player);
  }

  public Page<Mention> search(Pageable pageable) {
    return mentionRepository.findAll(pageable);
  }
}
