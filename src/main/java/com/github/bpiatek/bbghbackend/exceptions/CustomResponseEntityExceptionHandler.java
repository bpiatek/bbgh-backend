package com.github.bpiatek.bbghbackend.exceptions;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.github.bpiatek.bbghbackend.model.article.ArticleNotFoundException;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentNotFoundException;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Created by Bartosz Piatek on 26/07/2020
 */
@ControllerAdvice
@RestController
class CustomResponseEntityExceptionHandler {

  @ExceptionHandler(ArticleNotFoundException.class)
  ResponseEntity<ErrorResponse> handleArticleNotFoundException(ArticleNotFoundException ex) {
    return ResponseEntity
        .status(NOT_FOUND)
        .body(ErrorResponse.builder()
                  .message("Article with ID: " + ex.getId() + " not found")
                  .build());
  }

  @ExceptionHandler(CommentNotFoundException.class)
  ResponseEntity<ErrorResponse> handleCommentNotFoundException(CommentNotFoundException ex) {
    return ResponseEntity
        .status(NOT_FOUND)
        .body(ErrorResponse.builder()
                  .message("Comment with ID: " + ex.getId() + " not found")
                  .build());
  }

  @ExceptionHandler(InvalidFormatException.class)
  ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException ex) {
    return ResponseEntity
        .status(BAD_REQUEST)
        .body(ErrorResponse.builder()
                  .message(ex.getMessage())
                  .build());
  }

  @Builder
  @Value
  static class ErrorResponse {

    String message;
    LocalDateTime date = LocalDateTime.now();
  }
}
