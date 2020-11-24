package com.github.bpiatek.bbghbackend.model.mention;

import static java.util.stream.Collectors.toList;

import com.github.bpiatek.bbghbackend.model.mention.api.MentionNotFoundException;
import com.github.bpiatek.bbghbackend.model.mention.api.MentionResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Log4j2
@Service
@AllArgsConstructor
public class MentionFacade {
  private final MentionRepository mentionRepository;

  public Mention save(Mention player) {
    Mention mention = mentionRepository.save(player);
    log.info("Mention with ID: {} saved! Player ID: {}, comment ID: {}",
             mention.getId(),
             mention.getPlayer().getId(),
             mention.getComment().getId());

    return mention;
  }

  public Mention findById(Long id) {
    return mentionRepository.findById(id).orElseThrow(() -> new MentionNotFoundException(id));
  }

  public Page<MentionResponse> findAll(Pageable pageable) {
    Page<Mention> mentionsPageable = mentionRepository.findAll(pageable);

    List<MentionResponse> mentions = mentionsPageable
        .get()
        .map(Mention::toMentionResponse)
        .collect(toList());

    return new PageImpl<>(mentions, mentionsPageable.getPageable(), mentionsPageable.getTotalElements());
  }

  public Page<Mention> findByCommentId(Long commentId, Pageable pageable) {
    return mentionRepository.findByCommentId(commentId, pageable);
  }

  @Transactional
  public int setSentiment(Long id, MentionSentiment sentiment) {
    log.info("Setting mention (ID: {}) sentiment to: {}", id, sentiment.toString());
    return mentionRepository.setMentionSentimentById(id, sentiment);
  }
}

