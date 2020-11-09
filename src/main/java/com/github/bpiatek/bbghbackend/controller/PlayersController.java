package com.github.bpiatek.bbghbackend.controller;

import com.github.bpiatek.bbghbackend.model.player.Player;
import com.github.bpiatek.bbghbackend.model.player.PlayerFacade;
import com.github.bpiatek.bbghbackend.swagger.ApiPageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static org.mortbay.jetty.HttpStatus.ORDINAL_200_OK;

/**
 * @author Błażej Rybarkiewicz <b.rybarkiewicz@gmail.com>
 */
@Log4j2
@Api(tags = "Players controller")
@CrossOrigin
@RestController
@RequestMapping(value = "/api/players")
public class PlayersController {
  private final PlayerFacade playerFacade;

  public PlayersController(PlayerFacade playerFacade) {
    this.playerFacade = playerFacade;
  }

  @ApiOperation(value = "Search players")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Successfully retrieved all players"),
  })
  @ApiPageable
  @GetMapping
  Page<Player> searchPlayers(@ApiIgnore Pageable pageable) {
    return playerFacade.list(pageable);
  }
}
