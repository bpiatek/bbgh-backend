package com.github.bpiatek.bbghbackend.model.mention;

import static java.util.stream.Collectors.toList;

import com.github.bpiatek.bbghbackend.model.mention.api.*;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class MentionFacade {

  private final MentionRepository mentionRepository;
  private final MentionCreator mentionCreator;

  public Optional<MentionResponse> createAndSaveMention(CreateMentionRequest request) {
    return mentionCreator.from(request)
        .map(mentionRepository::save)
        .map(Mention::toMentionResponse);
  }

  public MentionResponse save(Mention player) {
    MentionResponse response = mentionRepository.save(player).toMentionResponse();
    logMentionSaved(response);

    return response;
  }

  public Mention findById(Long id) {
    log.debug("Looking for MENTION with ID: {}", id);
    return mentionRepository.findById(id).orElseThrow(() -> new MentionNotFoundException(id));
  }

  public Page<MentionResponse> search(Predicate predicate, Pageable pageable) {
    Page<Mention> allPageable = mentionRepository.findAll(predicate, pageable);
    List<MentionResponse> mentions = toMentionResponseList(allPageable);

    return new PageImpl<>(mentions, allPageable.getPageable(), allPageable.getTotalElements());
  }

  public Page<Mention> findByCommentId(Long commentId, Pageable pageable) {
    log.debug("Looking for MENTIONS by comment ID: {}", commentId);
    return mentionRepository.findByCommentId(commentId, pageable);
  }

  @Transactional
  public int setSentiment(Long id, MentionSentimentRequest request) {
    logSentimentIsSet(id, request);

    return mentionRepository.setMentionSentimentById(id, request.getMentionSentiment(), request.isHuman());
  }

  public Page<MentionResponse> findByPlayerId(Long playerId, Pageable pageable) {
    Page<Mention> mentionsByPlayerIdPageable = mentionRepository.findByPlayerIdIn(pageable, List.of(playerId));

    return new PageImpl<>(toMentionResponseList(mentionsByPlayerIdPageable),
                          mentionsByPlayerIdPageable.getPageable(),
                          mentionsByPlayerIdPageable.getTotalElements());
  }

  @Transactional
  public void setManySentimentsForManyMentions(MassMentionsSentimentsRequest request) {
    request.getItems()
        .forEach(this::setSentimentForItem);
  }

  private List<MentionResponse> toMentionResponseList(Page<Mention> mentions) {
    return mentions
        .get()
        .map(Mention::toMentionResponse)
        .collect(toList());
  }

  private void setSentimentForItem(MassMentionsSentimentsRequest.SpecificSentimentItem item) {
    mentionRepository.setSentimentForMentionsWithIds(item.getSentiment(), item.getIds());
  }

  public void logMentionSaved(MentionResponse response) {
    log.info("MENTION with ID: {} saved! Player ID: {}, comment ID: {}",
             response.getId(),
             response.getPlayerId(),
             response.getCommentId());
  }

  public void logSentimentIsSet(Long id, MentionSentimentRequest request) {
    if (request.isHuman()) {
      log.info("Human is setting MENTION (ID: {}) sentiment to: {}", id, request.getMentionSentiment());
    } else {
      log.info("Mentioner is setting MENTION (ID: {}) sentiment to: {}", id, request.getMentionSentiment());
    }
  }
}

