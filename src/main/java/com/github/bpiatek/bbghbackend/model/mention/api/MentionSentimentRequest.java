package com.github.bpiatek.bbghbackend.model.mention.api;

import com.github.bpiatek.bbghbackend.model.mention.MentionSentiment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Bartosz Piatek on 17/11/2020
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MentionSentimentRequest {
  MentionSentiment mentionSentiment;
  boolean human;
}
