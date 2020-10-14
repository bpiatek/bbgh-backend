package com.github.bpiatek.bbghbackend.model.player;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
public interface PlayerHtmlExtractor {
  Player getPlayer(String html, Integer id);
}
