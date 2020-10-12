package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.github.bpiatek.bbghbackend.model.player.Player;
import com.github.bpiatek.bbghbackend.model.player.PlayerFacade;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by Bartosz Piatek on 12/10/2020
 */
@Log4j2
@Service
@AllArgsConstructor
class NinetyMinutesPlayerCrawler {

  private final NinetyMinutesPlayerExtractor playerExtractor;
  private final PlayerFacade playerFacade;

  void startCrawlingForPlayers() {
    for(int i = calculateStartIndex(); i <= 42_140; i++) {
      final String html = getHtmlPageForPlayerWithId(i);
      Player player = playerExtractor.getPlayer(html, i);

      playerFacade.save(player);
    }
  }

  private String getHtmlPageForPlayerWithId(Integer id) {
    try {
      return Jsoup.connect("http://www.90minut.pl/kariera.php?id=" + id).get().html();
    } catch (IOException e) {
      log.warn("Connection error while parsing http://www.90minut.pl/kariera.php?id={}.\nMessage: {}.", id, e.getMessage());
    }

    return EMPTY;
  }

  private int calculateStartIndex() {
    Integer lastIndex = playerFacade.findLastSavedPlayer();
    if(lastIndex == null) {
      return 1;
    } else {
      return ++lastIndex;
    }
  }
}
