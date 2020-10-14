package com.github.bpiatek.bbghbackend.model.player;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
@Service
@AllArgsConstructor
public class PlayerFacade {

  private final PlayerRepository playerRepository;

  public Player save(Player player) {
    return playerRepository.save(player);
  }

  public Integer findLastSavedPlayer() {
    return playerRepository.findLastPlayerIdRead();
  }

}
