package com.github.bpiatek.bbghbackend.model.mention.api;

import lombok.Getter;

/**
 * Created by Bartosz Piatek on 18/11/2020
 */
@Getter
public class MentionNotFoundException extends RuntimeException {
  private final Long id;

  public MentionNotFoundException(Long id) {
    super("Mention with ID: " + id + " not found");
    this.id = id;
  }
}
