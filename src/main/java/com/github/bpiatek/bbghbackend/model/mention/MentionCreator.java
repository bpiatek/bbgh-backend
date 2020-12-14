package com.github.bpiatek.bbghbackend.model.mention;

import static com.github.bpiatek.bbghbackend.model.mention.MentionSentiment.NOT_CHECKED;
import static java.util.Optional.of;

import com.github.bpiatek.bbghbackend.model.comment.CommentFacade;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentNotFoundException;
import com.github.bpiatek.bbghbackend.model.mention.api.CreateMentionRequest;
import com.github.bpiatek.bbghbackend.model.mention.api.MentionCanNotBeCreatedException;
import com.github.bpiatek.bbghbackend.model.player.PlayerFacade;
import com.github.bpiatek.bbghbackend.model.player.api.PlayerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Bartosz Piatek on 14/12/2020
 */
@Log4j2
@Service
@RequiredArgsConstructor
class MentionCreator {

  private final CommentFacade commentFacade;
  private final PlayerFacade playerFacade;

  Optional<Mention> from(CreateMentionRequest request) {
    log.info("Creating MENTION for player with ID: {} in comment with ID: {}",
             request.getPlayerId(),
             request.getCommentId());

    try {
      return of(Mention.builder()
                    .comment(commentFacade.findById(request.getCommentId()))
                    .player(playerFacade.findById(request.getPlayerId()))
                    .sentiment(request.getSentiment() != null ? request.getSentiment() : NOT_CHECKED)
                    .startsAt(request.getStartsAt())
                    .endsAt(request.getEndsAt())
                    .build());
    } catch (PlayerNotFoundException | CommentNotFoundException ex) {
      log.info("MENTION for player with ID: {} in comment with ID: {} can not be created and saved. {}",
               request.getPlayerId(),
               request.getCommentId(),
               ex.getMessage());
      throw new MentionCanNotBeCreatedException(ex.getMessage());
    }
  }
}
