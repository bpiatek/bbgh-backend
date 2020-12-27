package com.github.bpiatek.bbghbackend.controller;

import static org.mortbay.jetty.HttpStatus.ORDINAL_200_OK;
import static org.mortbay.jetty.HttpStatus.ORDINAL_201_Created;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

import com.github.bpiatek.bbghbackend.model.mention.Mention;
import com.github.bpiatek.bbghbackend.model.mention.MentionFacade;
import com.github.bpiatek.bbghbackend.model.mention.MentionSentiment;
import com.github.bpiatek.bbghbackend.model.mention.api.*;
import com.github.bpiatek.bbghbackend.swagger.ApiPageable;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;

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
  Page<MentionResponse> search(@ApiIgnore Pageable pageable,
                               @RequestParam(required = false) List<Long> id,
                               @RequestParam(required = false) List<MentionSentiment> sentiment,
                               @QuerydslPredicate(root = Mention.class) Predicate predicate) {
    return mentionFacade.search(predicate, pageable);
  }

  @GetMapping("player")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved all mentions for specified player"),
  })
  @ApiPageable
  Page<MentionResponse> searchByPlayersFullName(@ApiIgnore Pageable pageable,
                                                @RequestParam String search) {

    return mentionFacade.findByPlayersName(search, pageable);
  }

  @ApiOperation(value = "Create mention.")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_201_Created, message = "Successfully created mention"),
  })
  @PostMapping
  ResponseEntity<MentionResponse> createMention(@RequestBody CreateMentionRequest request) {
    Optional<MentionResponse> response = mentionFacade.createAndSaveMention(request);
    response.ifPresent(mentionFacade::logMentionSaved);

    return response
        .map(mention -> ResponseEntity.status(CREATED).body(mention))
        .orElse(ResponseEntity.status(BAD_REQUEST).build());
  }

  @ApiOperation(value = "Set sentiment for given mention")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully set sentiment for mention"),
  })
  @PostMapping("{mentionId}/sentiment")
  ResponseEntity<Void> setMentionSentiment(@PathVariable Long mentionId, @RequestBody MentionSentimentRequest request) {
    mentionFacade.setSentiment(mentionId, request);

    return ResponseEntity.ok().build();
  }

  @ApiOperation(value = "Set many sentiments for many mentions")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully set sentiments for mentions"),
  })
  @PostMapping("sentiments")
  ResponseEntity<Void> setManyMentionSentiments(@RequestBody MassMentionsSentimentsRequest request) {
    mentionFacade.setManySentimentsForManyMentions(request);

    return ResponseEntity.ok().build();
  }
}
