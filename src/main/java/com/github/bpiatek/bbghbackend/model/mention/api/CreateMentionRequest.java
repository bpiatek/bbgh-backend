package com.github.bpiatek.bbghbackend.model.mention.api;

import com.github.bpiatek.bbghbackend.model.mention.MentionSentiment;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Getter
@NoArgsConstructor
public class CreateMentionRequest {
  Long commentId;
  Long playerId;
  MentionSentiment sentiment;
  int startsAt;
  int endsAt;
}
