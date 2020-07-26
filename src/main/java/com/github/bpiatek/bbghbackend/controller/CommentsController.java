package com.github.bpiatek.bbghbackend.controller;

import static org.mortbay.jetty.HttpStatus.ORDINAL_200_OK;

import com.github.bpiatek.bbghbackend.model.comment.Comment;
import com.github.bpiatek.bbghbackend.model.comment.CommentFacade;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentOpinionRequest;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  CommentsController(CommentFacade commentFacade) {
    this.commentFacade = commentFacade;
  }

  @ApiOperation(value = "Get comment by ID", response = Comment.class)
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved comment by ID"),
  })
  @GetMapping("comment/{commentId}")
  ResponseEntity<Comment> getCommentById(@PathVariable Long commentId) {
    return ResponseEntity.ok().body(commentFacade.findById(commentId));
  }

  @ApiOperation(value = "Set commentOpinionStatus for a comment")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully set comment opinion status"),
  })
  @PostMapping("comment/{commentId}/status")
  ResponseEntity<Void> setCommentOpinionStatus(@PathVariable Long commentId, @RequestBody CommentOpinionRequest opinionRequest) {
    commentFacade.setCommentOpinionStatus(commentId, opinionRequest.getStatus());
    return ResponseEntity.ok().build();
  }
}
