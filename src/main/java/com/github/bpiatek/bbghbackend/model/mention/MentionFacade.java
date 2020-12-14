package com.github.bpiatek.bbghbackend.model.mention;

import static java.util.stream.Collectors.toList;

import com.github.bpiatek.bbghbackend.model.mention.api.*;
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

  public Page<MentionResponse> search(Pageable pageable, List<MentionSentiment> sentiments, List<Long> ids) {
    log.debug("Searching for MENTIONS...");
    if (sentiments != null) {
      return findBySentiments(pageable, sentiments);
    } else if (ids != null) {
      return findByPlayersIds(pageable, ids);
    } else {
      return findAll(pageable);
    }
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

  private Page<MentionResponse> findBySentiments(Pageable pageable, List<MentionSentiment> sentiments) {
    log.debug("Looking for MENTIONS by sentiment: {}", sentiments.toString());
    Page<Mention> mentionsWithSentimentsPageable = mentionRepository.findBySentimentIn(pageable, sentiments);
    List<MentionResponse> mentions = toMentionResponseList(mentionsWithSentimentsPageable);

    return new PageImpl<>(
        mentions,
        mentionsWithSentimentsPageable.getPageable(),
        mentionsWithSentimentsPageable.getTotalElements()
    );
  }

  private Page<MentionResponse> findByPlayersIds(Pageable pageable, List<Long> ids) {
    log.debug("Looking for MENTIONS by players ids: {}", ids.toString());
    Page<Mention> mentionsWithPlayersIdsPageable = mentionRepository.findByPlayerIdIn(pageable, ids);
    List<MentionResponse> mentions = toMentionResponseList(mentionsWithPlayersIdsPageable);

    return new PageImpl<>(
        mentions,
        mentionsWithPlayersIdsPageable.getPageable(),
        mentionsWithPlayersIdsPageable.getTotalElements()
    );
  }

  private Page<MentionResponse> findAll(Pageable pageable) {
    Page<Mention> mentionsPageable = mentionRepository.findAll(pageable);

    List<MentionResponse> mentions = mentionsPageable
        .get()
        .map(Mention::toMentionResponse)
        .collect(toList());

    return new PageImpl<>(mentions, mentionsPageable.getPageable(), mentionsPageable.getTotalElements());
  }

  private List<MentionResponse> toMentionResponseList(Page<Mention> mentions) {
    return mentions
        .get()
        .map(Mention::toMentionResponse)
        .collect(toList());
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

