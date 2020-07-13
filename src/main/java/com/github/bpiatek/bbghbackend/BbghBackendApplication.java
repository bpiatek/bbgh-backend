package com.github.bpiatek.bbghbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BbghBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(BbghBackendApplication.class, args);
  }

}
