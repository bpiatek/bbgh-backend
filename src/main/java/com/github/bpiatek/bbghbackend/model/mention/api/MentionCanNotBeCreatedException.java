package com.github.bpiatek.bbghbackend.model.mention.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Bartosz Piatek on 03/12/2020
 */
@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class MentionCanNotBeCreatedException extends RuntimeException {

  public MentionCanNotBeCreatedException(String message) {
    super("Mention can not be created and saved: " + message);
  }
}
