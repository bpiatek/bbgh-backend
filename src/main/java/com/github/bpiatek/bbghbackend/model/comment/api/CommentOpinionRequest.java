package com.github.bpiatek.bbghbackend.model.comment.api;

import com.github.bpiatek.bbghbackend.model.comment.CommentOpinionStatus;
import lombok.*;

/**
 * Created by Bartosz Piatek on 26/07/2020
 */
@Getter
//@AllArgsConstructor
@NoArgsConstructor
public class CommentOpinionRequest {
  CommentOpinionStatus status;
}
