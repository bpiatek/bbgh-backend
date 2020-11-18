package com.github.bpiatek.bbghbackend.model.mention;

import static java.util.stream.Collectors.toList;

import com.github.bpiatek.bbghbackend.model.mention.api.MentionNotFoundException;
import com.github.bpiatek.bbghbackend.model.mention.api.MentionResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

  public Mention findById(Long id) {
    return mentionRepository.findById(id).orElseThrow(() -> new MentionNotFoundException(id));
  }

  public Page<MentionResponse> findAll(Pageable pageable) {
    List<MentionResponse> mentions = mentionRepository.findAll(pageable)
        .get()
        .map(Mention::toMentionResponse)
        .collect(toList());

    return new PageImpl<>(mentions, pageable, mentions.size());
  }

  public Page<Mention> findByCommentId(Long commentId, Pageable pageable) {
    return mentionRepository.findByCommentId(commentId, pageable);
  }

  @Transactional
  public int setSentiment(Long id, MentionSentiment sentiment) {
    return mentionRepository.setMentionSentimentById(id, sentiment);
  }
}
