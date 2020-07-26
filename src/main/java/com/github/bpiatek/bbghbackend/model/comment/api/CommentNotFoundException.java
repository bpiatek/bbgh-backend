package com.github.bpiatek.bbghbackend.model.comment.api;

import lombok.Getter;

/**
 * Created by Bartosz Piatek on 26/07/2020
 */
@Getter
public class CommentNotFoundException extends RuntimeException{

  private final Long id;

  public CommentNotFoundException(Long id) {
    super("Comment with ID: " + id + " not found");
    this.id = id;
  }
}
