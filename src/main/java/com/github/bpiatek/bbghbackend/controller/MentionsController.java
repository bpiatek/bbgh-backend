package com.github.bpiatek.bbghbackend.controller;

import static com.github.bpiatek.bbghbackend.model.mention.MentionSentiment.NOT_CHECKED;
import static org.mortbay.jetty.HttpStatus.ORDINAL_200_OK;
import static org.mortbay.jetty.HttpStatus.ORDINAL_201_Created;
import static org.springframework.http.HttpStatus.CREATED;

import com.github.bpiatek.bbghbackend.model.comment.CommentFacade;
import com.github.bpiatek.bbghbackend.model.mention.Mention;
import com.github.bpiatek.bbghbackend.model.mention.MentionFacade;
import com.github.bpiatek.bbghbackend.model.mention.MentionSentiment;
import com.github.bpiatek.bbghbackend.model.mention.api.CreateMentionRequest;
import com.github.bpiatek.bbghbackend.model.mention.api.MentionResponse;
import com.github.bpiatek.bbghbackend.model.mention.api.MentionSentimentRequest;
import com.github.bpiatek.bbghbackend.model.player.PlayerFacade;
import com.github.bpiatek.bbghbackend.swagger.ApiPageable;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Log4j2
@Api(tags = "Mentions controller")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/mentions")
class MentionsController {

  private final MentionFacade mentionFacade;
  private final CommentFacade commentFacade;
  private final PlayerFacade playerFacade;

  @ApiOperation(value = "Get mention by ID")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved mention by ID"),
  })
  @ApiPageable
  @GetMapping("{id}")
  ResponseEntity<MentionResponse> getMentionById(@PathVariable Long id) {
    return ResponseEntity.ok(mentionFacade.findById(id).toMentionResponse());
  }

  @ApiOperation(value = "Search for mentions")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved all mentions"),
  })
  @ApiPageable
  @GetMapping
  Page<MentionResponse> search(@ApiIgnore Pageable pageable, @RequestParam(required = false) List<MentionSentiment> sentiments) {
    return mentionFacade.search(pageable, sentiments);
  }

  @ApiOperation(value = "Create mention.")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_201_Created, message = "Successfully created mention"),
  })
  @PostMapping
  ResponseEntity<Mention> createMention(@RequestBody CreateMentionRequest createMentionRequest) {

    log.info("Creating mention for player with ID: {} in comment with ID: {}",
        createMentionRequest.getPlayerId(),
        createMentionRequest.getCommentId());

    Mention mention = Mention.builder()
        .comment(commentFacade.findById(createMentionRequest.getCommentId()))
        .player(playerFacade.findById(createMentionRequest.getPlayerId()))
        .sentiment(createMentionRequest.getSentiment() != null ? createMentionRequest.getSentiment() : NOT_CHECKED)
        .startsAt(createMentionRequest.getStartsAt())
        .endsAt(createMentionRequest.getEndsAt())
        .build();

    Mention savedMention = mentionFacade.save(mention);

    return ResponseEntity.status(CREATED).body(savedMention);
  }

  @ApiOperation(value = "Set sentiment for given mention")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully set sentiment for mention"),
  })
  @PostMapping("{mentionId}/sentiment")
  ResponseEntity<Void> setMentionSentiment(@PathVariable Long mentionId, @RequestBody MentionSentimentRequest request) {
    mentionFacade.setSentiment(mentionId, request.getMentionSentiment());

    return ResponseEntity.ok().build();
  }
}
