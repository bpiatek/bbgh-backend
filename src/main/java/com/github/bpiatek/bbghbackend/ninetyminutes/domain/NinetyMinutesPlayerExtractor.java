package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import com.github.bpiatek.bbghbackend.model.player.Player;
import com.github.bpiatek.bbghbackend.model.player.PlayerHtmlExtractor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */

@Service
@AllArgsConstructor
class NinetyMinutesPlayerExtractor implements PlayerHtmlExtractor {

  private final PlayerCreator playerCreator;

  @Override
  public Player getPlayer(String html, Integer id) {
    return playerCreator.createFromHtml(html, id);
  }
}
