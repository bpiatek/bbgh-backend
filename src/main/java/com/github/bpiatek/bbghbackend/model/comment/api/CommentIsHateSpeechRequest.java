package com.github.bpiatek.bbghbackend.model.comment.api;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@NoArgsConstructor
@AllArgsConstructor
public class CommentIsHateSpeechRequest {
  boolean isHateSpeech;

  public boolean getIsHateSpeech() {
    return this.isHateSpeech;
  }

  public void setIsHateSpeech(boolean isHateSpeech) {
    this.isHateSpeech = isHateSpeech;
  }
}
