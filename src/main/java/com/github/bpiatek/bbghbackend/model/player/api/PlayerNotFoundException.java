package com.github.bpiatek.bbghbackend.model.player.api;

import lombok.Getter;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Getter
public class PlayerNotFoundException extends RuntimeException{

  private final Long id;

  public PlayerNotFoundException(Long id) {
    super("Player with ID: " + id + " not found");
    this.id = id;
  }
}
