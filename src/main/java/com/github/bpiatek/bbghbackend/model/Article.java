package com.github.bpiatek.bbghbackend.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String url;
    private String title;
    private LocalDateTime creationDate;
    private String content;
    @Transient
    private List<Comment> comments;
}
