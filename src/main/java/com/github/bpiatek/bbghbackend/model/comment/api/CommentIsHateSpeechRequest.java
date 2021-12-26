package com.github.bpiatek.bbghbackend.model.comment.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentIsHateSpeechRequest {
  boolean isHateSpeech;
}
