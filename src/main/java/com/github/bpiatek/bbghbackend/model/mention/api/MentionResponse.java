package com.github.bpiatek.bbghbackend.model.mention.api;

import com.github.bpiatek.bbghbackend.model.mention.MentionSentiment;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Created by Bartosz Piatek on 18/11/2020
 */
@Value
@Builder
public class MentionResponse {
  Long id;
  Long articleId;
  Long commentId;
  String commentContent;
  LocalDateTime commentDate;
  Long playerId;
  String playerFullName;
  MentionSentiment mentionSentiment;
}
