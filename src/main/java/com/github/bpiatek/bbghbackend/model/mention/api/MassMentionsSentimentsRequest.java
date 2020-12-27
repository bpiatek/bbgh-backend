package com.github.bpiatek.bbghbackend.model.mention.api;

import com.github.bpiatek.bbghbackend.model.mention.MentionSentiment;
import lombok.*;

import java.util.List;

/**
 * Created by Bartosz Piatek on 27/12/2020
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MassMentionsSentimentsRequest {
  private List<SpecificSentimentItem> items;


  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SpecificSentimentItem {
    private MentionSentiment sentiment;
    private List<Long> ids;
  }
}
