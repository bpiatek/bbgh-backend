package com.github.bpiatek.bbghbackend.model.player;

import com.github.bpiatek.bbghbackend.model.comment.Comment;
import com.github.bpiatek.bbghbackend.model.comment.api.CommentNotFoundException;
import com.github.bpiatek.bbghbackend.model.player.api.PlayerNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

}
