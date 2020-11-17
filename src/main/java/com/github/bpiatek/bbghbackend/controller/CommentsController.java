package com.github.bpiatek.bbghbackend.controller;

import static org.mortbay.jetty.HttpStatus.ORDINAL_200_OK;

import com.github.bpiatek.bbghbackend.model.comment.Comment;
import com.github.bpiatek.bbghbackend.model.comment.CommentFacade;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentResponse;
import com.github.bpiatek.bbghbackend.model.mention.Mention;
import com.github.bpiatek.bbghbackend.model.mention.MentionFacade;
import com.github.bpiatek.bbghbackend.swagger.ApiPageable;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Bartosz Piatek on 12/07/2020
 */
@Log4j2
@Api(tags = "Comments controller")
@CrossOrigin
@RestController
@RequestMapping(value = "/api/")
class CommentsController {

  private final CommentFacade commentFacade;
  private final MentionFacade mentionFacade;

  CommentsController(CommentFacade commentFacade, MentionFacade mentionFacade) {
    this.commentFacade = commentFacade;
    this.mentionFacade = mentionFacade;
  }

  @ApiOperation(value = "Get comment by ID", response = Comment.class)
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved comment by ID"),
  })
  @GetMapping("comment/{commentId}")
  ResponseEntity<CommentResponse> getCommentById(@PathVariable Long commentId) {
    return ResponseEntity.ok().body(commentFacade.findById(commentId).toCommentResponse());
  }

  @ApiOperation(value = "Get all mentions for given comment")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved mentions for a given comment"),
  })
  @ApiPageable
  @GetMapping("comment/{commentId}/mentions")
  Page<Mention> getAllMentionsForCommentPageable(@PathVariable Long commentId, @ApiIgnore Pageable pageable) {
    return mentionFacade.findByCommentId(commentId, pageable);
  }
}
