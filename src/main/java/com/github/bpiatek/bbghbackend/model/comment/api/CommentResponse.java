package com.github.bpiatek.bbghbackend.model.comment.api;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Created by Bartosz Piatek on 17/11/2020
 */
@Value
@Builder
public class CommentResponse {
  Long id;
  String author;
  LocalDateTime dateAdded;
  String content;
  Long articleId;
  Boolean isHateSpeech;
}
