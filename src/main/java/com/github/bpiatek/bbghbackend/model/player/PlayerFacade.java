package com.github.bpiatek.bbghbackend.model.player;

import com.github.bpiatek.bbghbackend.model.player.api.PlayerNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  public Player findById(Long id) {
    return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
  }

  public Integer findLastSavedPlayer() {
    return playerRepository.findLastPlayerIdRead();
  }

  public Page<Player> list(Pageable pageable) {
    return playerRepository.findAll(pageable);
  }
}
