package com.github.bpiatek.bbghbackend.controller;

import static org.mortbay.jetty.HttpStatus.ORDINAL_202_Accepted;
import static org.mortbay.jetty.HttpStatus.ORDINAL_500_Internal_Server_Error;

import com.github.bpiatek.bbghbackend.ninetyminutes.domain.NinetyMinutesFacade;
import io.swagger.annotations.*;
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
      return ResponseEntity.accepted().build();
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.status(ORDINAL_500_Internal_Server_Error).build();
    }
  }
}
