package com.github.bpiatek.bbghbackend.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class Comment {
    private String author;
    //  private LocalDateTime dateAdded;
    private String content;
}
