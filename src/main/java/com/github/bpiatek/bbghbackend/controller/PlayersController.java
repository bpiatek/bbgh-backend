package com.github.bpiatek.bbghbackend.controller;

import static org.mortbay.jetty.HttpStatus.ORDINAL_200_OK;

import com.github.bpiatek.bbghbackend.model.mention.Mention;
import com.github.bpiatek.bbghbackend.model.mention.MentionFacade;
import com.github.bpiatek.bbghbackend.model.player.Player;
import com.github.bpiatek.bbghbackend.model.player.PlayerFacade;
import com.github.bpiatek.bbghbackend.model.player.SentimentCounter;
import com.github.bpiatek.bbghbackend.swagger.ApiPageable;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Log4j2
@Api(tags = "Players controller")
@CrossOrigin
@RestController
@RequestMapping(value = "/api/players")
@RequiredArgsConstructor
class PlayersController {

  private final PlayerFacade playerFacade;
  private final MentionFacade mentionFacade;

  @ApiOperation(value = "Get player by ID")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved player by ID"),
  })
  @ApiPageable
  @GetMapping("{id}")
  ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
    return ResponseEntity.ok(playerFacade.findById(id));
  }

  @ApiOperation(value = "Get all players")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved all players"),
  })
  @ApiPageable
  @GetMapping
  Page<Player> getAll(@ApiIgnore Pageable pageable) {
    return playerFacade.findAll(pageable);
  }

  @ApiOperation(value = "Search for players")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved searched players"),
  })
  @ApiPageable
  @GetMapping("search")
  Page<Player> findByName(@RequestParam String s,  @ApiIgnore Pageable pageable) {
   return playerFacade.search(s, pageable);
  }

  @ApiOperation(value = "Get player mentions ratio")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved player's ratio"),
  })
  @ApiPageable
  @GetMapping("{playerId}/ratio")
  SentimentCounter playerPercentage(@PathVariable Long playerId) {
    final List<Mention> mentions = mentionFacade.findAllByByPlayerId(playerId);

    return playerFacade.playerPercentage(mentions);
  }
}
