package com.github.bpiatek.bbghbackend.model.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.bpiatek.bbghbackend.model.comment.Comment;
import com.querydsl.core.annotations.QueryEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.*;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@QueryEntity
public class Article {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String url;
  private String title;
  private LocalDateTime creationDate;
  private LocalDateTime updatedAt;
  private String content;

  @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JsonIgnore
  @ToString.Exclude
  private List<Comment> comments = new ArrayList<>();

  public void setComments(List<Comment> comments) {
    comments.stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toList())
        .forEach(comment -> comment.setArticle(this));

    this.comments = comments;
  }

  public void addComment(Comment comment) {
    this.comments.add(comment);
  }
}
