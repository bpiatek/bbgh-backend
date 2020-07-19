package com.github.bpiatek.bbghbackend.controller;

import com.github.bpiatek.bbghbackend.ninetyminutes.domain.NinetyMinutesFacade;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.mortbay.jetty.HttpStatus.*;

/**
 * Created by Bartosz Piatek on 12/07/2020
 */
@Log4j2
@Api(tags = "Crawlers for specific portals")
@RestController
@RequestMapping(value = "/api/crawlers")
class CrawlersController {

  private final NinetyMinutesFacade facade;

  CrawlersController(NinetyMinutesFacade facade) {
    this.facade = facade;
  }

  @ApiOperation(value = "Run crawler for 90minut.pl")
  @ApiResponses(value = {
      @ApiResponse(code = ORDINAL_202_Accepted, message = "Crawler successfully started"),
  })
  @PostMapping("/run/90minutes")
  ResponseEntity<Void> runCrawler() {
    try {
      facade.runCrawler();
      log.info("Crawler for portal 90minut.pl started manually.");
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
    facade.stopCrawler();
    log.info("Crawler for portal 90minut.pl stopped manually.");
    return ResponseEntity.ok().build();
  }
}
