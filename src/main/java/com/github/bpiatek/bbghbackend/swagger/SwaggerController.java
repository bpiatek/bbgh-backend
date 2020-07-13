package com.github.bpiatek.bbghbackend.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Created by Bartosz Piatek on 13/07/2020
 */
@ApiIgnore
@Controller
@RequestMapping("/swagger")
class SwaggerController {
  @GetMapping
  public String greeting() {
    return "redirect:/swagger-ui.html";
  }
}
