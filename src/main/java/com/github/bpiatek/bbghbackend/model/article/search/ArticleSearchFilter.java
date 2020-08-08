package com.github.bpiatek.bbghbackend.model.article.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@AllArgsConstructor
@Getter
public class ArticleSearchFilter {
  private final String property;
  private final String operation;
  private final String value;
}
