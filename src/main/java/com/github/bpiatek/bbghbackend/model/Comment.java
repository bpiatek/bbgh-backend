package com.github.bpiatek.bbghbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String author;
//  private LocalDateTime dateAdded;
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonBackReference
  private Article article;

  public Comment(String author, String content) {
    this.author = author;
    this.content = content;
  }

  @Override
  public String toString() {
    return "Comment{" +
           "id=" + id +
           ", author='" + author + '\'' +
           ", content='" + content + '\'' +
           '}';
  }
}
