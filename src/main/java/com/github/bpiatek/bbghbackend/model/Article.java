package com.github.bpiatek.bbghbackend.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Builder
@ToString
@Getter
public class Article {
  private String url;
  private String title;
  private LocalDateTime creationDate;
  private String content;
  private List<Comment> comments;
}
