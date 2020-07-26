package com.github.bpiatek.bbghbackend.model.article;

import lombok.Getter;

/**
 * Created by Bartosz Piatek on 26/07/2020
 */
@Getter
public class ArticleNotFoundException extends RuntimeException {
  private final Long id;

  public ArticleNotFoundException(Long id) {
    super("Article with ID: " + id + " not found");
    this.id = id;
  }
}
