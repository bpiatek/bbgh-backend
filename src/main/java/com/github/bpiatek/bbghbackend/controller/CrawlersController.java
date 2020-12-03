package com.github.bpiatek.bbghbackend.controller;

import static org.mortbay.jetty.HttpStatus.ORDINAL_200_OK;
import static org.mortbay.jetty.HttpStatus.ORDINAL_202_Accepted;
import static org.mortbay.jetty.HttpStatus.ORDINAL_500_Internal_Server_Error;

import com.github.bpiatek.bbghbackend.ninetyminutes.domain.NinetyMinutesFacade;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Bartosz Piatek on 12/07/2020
 */
@Log4j2
@Api(tags = "Crawlers for specific portals")
@RestController
@RequestMapping(value = "/api/crawlers")
@RequiredArgsConstructor
class CrawlersController {

  private final NinetyMinutesFacade ninetyMinutesFacade;

  @ApiOperation(value = "Run crawler for 90minut.pl")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_202_Accepted, message = "Crawler successfully started"),
  })
  @PostMapping("/run/90minutes")
  ResponseEntity<Void> runCrawler() {
    try {
      log.info("Crawler for portal 90minut.pl started manually.");
      ninetyMinutesFacade.runCrawler();
      return ResponseEntity.accepted().build();
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.status(ORDINAL_500_Internal_Server_Error).build();
    }
  }

  @ApiOperation(value = "Stop crawler for 90minut.pl")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_200_OK, message = "Crawler successfully stopped"),
  })
  @PostMapping("/stop/90minutes")
  ResponseEntity<Void> stopCrawler() {
    log.info("Crawler for portal 90minut.pl stopped manually.");
    ninetyMinutesFacade.stopCrawler();
    return ResponseEntity.ok().build();
  }

  @ApiOperation(value = "Run players crawler for 90minut.pl")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_202_Accepted, message = "Crawler successfully started"),
  })
  @PostMapping("/run/90minutes/players")
  ResponseEntity<Void> runPlayersCrawler() {
    try {
      log.info("Crawler for portal 90minut.pl started manually.");

      Runnable runnable = ninetyMinutesFacade::runPlayersCrawler;
      final Thread thread = new Thread(runnable);
      thread.start();

      return ResponseEntity.accepted().build();
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.status(ORDINAL_500_Internal_Server_Error).build();
    }
  }
}
