package com.github.bpiatek.bbghbackend.ninetyminutes.domain;

import static com.github.bpiatek.bbghbackend.ninetyminutes.domain.NinetyMinutesCrawlerController.NINETY_MINUTES_URL;
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

  private static final String PLAYER_URL = "http://www.90minut.pl/kariera.php?id=";

  private final NinetyMinutesPlayerExtractor playerExtractor;
  private final PlayerFacade playerFacade;

  void startCrawlingForPlayers() {
    for(int i = calculateStartIndex(); i <= 42_140; i++) {
      String html = getHtmlPageForPlayerWithId(i);
      savePlayerFromHtml(html, i);
    }
  }

  private int calculateStartIndex() {
    Integer lastIndex = playerFacade.findLastSavedPlayer();
    if(lastIndex == null) {
      return 1;
    } else {
      return ++lastIndex;
    }
  }

  private String getHtmlPageForPlayerWithId(Integer id) {
    try {
      return Jsoup.connect(PLAYER_URL + id).get().html();
    } catch (IOException e) {
      log.warn("Connection error while parsing PLAYER_URL{}.\nMessage: {}.", id, e.getMessage());
    }

    return EMPTY;
  }

  private void savePlayerFromHtml(String html, int i) {
    if(!html.isEmpty()) {
      Player player = playerExtractor.getPlayer(html, i);
      if(player != null) {
        Player savedPlayer = playerFacade.save(player);
        log.info("Player with ID: {} from portal: {} saved", savedPlayer.getId(), NINETY_MINUTES_URL);
      } else {
        log.info("No details about player with URL ID: {} from portal {}", i, NINETY_MINUTES_URL);
      }
    }
  }
}
