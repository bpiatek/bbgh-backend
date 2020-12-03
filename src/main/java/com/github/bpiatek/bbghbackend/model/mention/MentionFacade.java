package com.github.bpiatek.bbghbackend.model.mention;

import static com.github.bpiatek.bbghbackend.model.mention.MentionSentiment.NOT_CHECKED;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import com.github.bpiatek.bbghbackend.model.comment.Comment;
import com.github.bpiatek.bbghbackend.model.comment.CommentFacade;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentNotFoundException;
import com.github.bpiatek.bbghbackend.model.mention.api.*;
import com.github.bpiatek.bbghbackend.model.player.Player;
import com.github.bpiatek.bbghbackend.model.player.PlayerFacade;
import com.github.bpiatek.bbghbackend.model.player.api.PlayerNotFoundException;
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
  private final CommentFacade commentFacade;
  private final PlayerFacade playerFacade;

  public Optional<MentionResponse> createAndSaveMention(CreateMentionRequest request) {
    return constructMention(request)
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
  public int setSentiment(Long id, MentionSentiment sentiment) {
    log.info("Setting MENTION (ID: {}) sentiment to: {}", id, sentiment.toString());
    return mentionRepository.setMentionSentimentById(id, sentiment);
  }

  public void logMentionSaved(MentionResponse response) {
    log.info("MENTION with ID: {} saved! Player ID: {}, comment ID: {}",
             response.getId(),
             response.getPlayerId(),
             response.getCommentId());
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

  private Optional<Mention> constructMention(CreateMentionRequest request) {
    log.info("Creating MENTION for player with ID: {} in comment with ID: {}",
             request.getPlayerId(),
             request.getCommentId());

    try {
      return of(Mention.builder()
                    .comment(getComment(request.getCommentId()))
                    .player(getPlayer(request.getPlayerId()))
                    .sentiment(request.getSentiment() != null ? request.getSentiment() : NOT_CHECKED)
                    .startsAt(request.getStartsAt())
                    .endsAt(request.getEndsAt())
                    .build());
    } catch (MentionCanNotBeCreatedException ex) {
      log.error(ex.getMessage());
    }

    return Optional.empty();
  }

  private Player getPlayer(Long id) {
    try {
      return playerFacade.findById(id);
    } catch (PlayerNotFoundException ex) {
      throw new MentionCanNotBeCreatedException(ex.getMessage());
    }
  }

  private Comment getComment(Long id) {
    try {
      return commentFacade.findById(id);
    } catch (CommentNotFoundException ex) {
      throw new MentionCanNotBeCreatedException(ex.getMessage());
    }
  }
}

