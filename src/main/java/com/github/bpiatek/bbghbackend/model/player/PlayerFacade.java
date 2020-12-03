package com.github.bpiatek.bbghbackend.model.player;

import com.github.bpiatek.bbghbackend.model.player.api.PlayerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class PlayerFacade {

  private final PlayerRepository playerRepository;
  private final PlayerSearcher playerSearcher;

  public Player save(Player player) {
    return playerRepository.save(player);
  }

  public Player findById(Long id) {
    log.debug("Looking for PLAYER with ID: {}", id);
    return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(id));
  }

  public Integer findLastSavedPlayer() {
    log.debug("Find last saved PLAYER ID in database");
    return playerRepository.findLastPlayerIdRead();
  }

  public Page<Player> findAll(Pageable pageable) {
    return playerRepository.findAll(pageable);
  }

  public Page<Player> search(String text, Pageable pageable) {
    return playerSearcher.search(text, pageable);
  }
}
