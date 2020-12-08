package com.github.bpiatek.bbghbackend.model.mention;

import com.github.bpiatek.bbghbackend.model.comment.Comment;
import com.github.bpiatek.bbghbackend.model.mention.api.MentionResponse;
import com.github.bpiatek.bbghbackend.model.player.Player;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mention {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  private Comment comment;

  @ManyToOne(fetch = FetchType.EAGER)
  private Player player;

  @Enumerated(STRING)
  private MentionSentiment sentiment;

  private Boolean sentimentMarkedByHuman;

  /**
   * Index of first character of mention in a comment
   * e.g. comment: "Good game John Smith", mention is for player "John Smith" and  mention.startsAt=10, mention.endsAt=19
   */
  private int startsAt;
  private int endsAt;

  public MentionResponse toMentionResponse() {
    return MentionResponse.builder()
        .id(this.id)
        .commentId(this.comment.getId())
        .commentContent(this.comment.getContent())
        .commentDate(this.comment.getDateAdded())
        .articleId(this.comment.getArticle().getId())
        .playerId(this.player.getId())
        .playerFullName(this.player.getFullName())
        .mentionSentiment(this.sentiment)
        .startsAt(this.startsAt)
        .endsAt(this.endsAt)
        .sentimentMarkedByHuman(this.sentimentMarkedByHuman)
        .build();
  }

  @Override
  public String toString() {
    return "Mention{" +
           "id=" + id +
           ", comment=" + comment +
           ", player=" + player +
           ", sentiment=" + sentiment +
           ", sentimentMarkedByHuman=" + sentimentMarkedByHuman +
           ", startsAt=" + startsAt +
           ", endsAt=" + endsAt +
           '}';
  }
}
