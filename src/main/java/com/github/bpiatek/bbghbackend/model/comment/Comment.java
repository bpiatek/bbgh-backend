package com.github.bpiatek.bbghbackend.model.comment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.bpiatek.bbghbackend.model.article.Article;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentResponse;
import com.querydsl.core.annotations.QueryEntity;
import lombok.*;

import java.time.LocalDateTime;

import javax.persistence.*;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@QueryEntity
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String author;
  private LocalDateTime dateAdded;
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  @EqualsAndHashCode.Exclude
  private Article article;

  public Comment(String author, String content, LocalDateTime dateAdded) {
    this.author = author;
    this.content = content;
    this.dateAdded = dateAdded;
  }

  public CommentResponse toCommentResponse() {
    return CommentResponse.builder()
        .id(this.id)
        .author(this.author)
        .content(this.content)
        .dateAdded(this.dateAdded)
        .articleId(this.article.getId())
        .build();
  }

  @Override
  public String toString() {
    return "Comment{" +
           "id=" + id +
           ", author='" + author + '\'' +
           ", content='" + content + '\'' +
           ", dateAdded='" + dateAdded + '\'' +
           '}';
  }
}
