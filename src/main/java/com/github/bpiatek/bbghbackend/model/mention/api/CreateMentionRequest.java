package com.github.bpiatek.bbghbackend.model.mention.api;

import com.github.bpiatek.bbghbackend.model.mention.MentionSentiment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMentionRequest {
  private Long commentId;
  private Long playerId;
  private MentionSentiment sentiment;
  private int startsAt;
  private int endsAt;
}
