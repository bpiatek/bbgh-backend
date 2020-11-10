package com.github.bpiatek.bbghbackend.model.mention;

import com.github.bpiatek.bbghbackend.model.comment.Comment;
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

  /**
   * Index of first character of mention in a comment
   * e.g. comment: "Good game John Smith", mention is for player "John Smith" and  mention.startsAt=10, mention.endsAt=19
   */
  private int startsAt;
  private int endsAt;

  @Override
  public String toString() {
    return "Mention{" +
        "id=" + id +
        ", comment=" + comment +
        ", player=" + player +
        ", sentiment=" + sentiment +
        ", startsAt=" + startsAt +
        ", endsAt=" + endsAt +
        '}';
  }
}
