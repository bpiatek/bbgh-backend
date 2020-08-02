package com.github.bpiatek.bbghbackend.model.article.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Data
@AllArgsConstructor
@Builder
public class ArticleSearchResult {
  private final long id;
  private final String url;
  private final String title;
  private final LocalDateTime creationDate;
  private final String content;
  private final long comments;
  private final long positive;
  private final long neutral;
  private final long negative;
  private final long notOpinion;
  private final long notChecked;
}
