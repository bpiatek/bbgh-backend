package com.github.bpiatek.bbghbackend.model;

import lombok.*;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Comment {
  private String author;
//  private LocalDateTime dateAdded;
  private String content;
}
