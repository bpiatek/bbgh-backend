package com.github.bpiatek.bbghbackend.ninetyminutes;

import com.github.bpiatek.bbghbackend.ninetyminutes.domain.NinetyMinutesFacade;
import lombok.extern.log4j.Log4j2;
import org.mortbay.jetty.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Bartosz Piatek on 10/07/2020
 */
@Log4j2
@RestController
@RequestMapping(value = "/api/ninety-minutes")
class NinetyMinutesController {

  private final NinetyMinutesFacade facade;

  NinetyMinutesController(NinetyMinutesFacade facade) {
    this.facade = facade;
  }

  @PostMapping("/run")
  ResponseEntity<Void> runCrawler() {
    try {
      facade.runCrawler();
      return ResponseEntity.accepted().build();
    } catch (Exception e) {
      log.warn(e.getMessage());
      return ResponseEntity.status(HttpStatus.ORDINAL_500_Internal_Server_Error).build();
    }
  }

}
