package com.github.bpiatek.bbghbackend;

import com.github.bpiatek.bbghbackend.ninetyminutes.NinetyMinutesCrawlerController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BbghBackendApplication implements CommandLineRunner {

  private final NinetyMinutesCrawlerController ninetyMinutesCrawlerController;

  public BbghBackendApplication(NinetyMinutesCrawlerController ninetyMinutesCrawlerController) {
    this.ninetyMinutesCrawlerController
        = ninetyMinutesCrawlerController;
  }

  public static void main(String[] args) {
    SpringApplication.run(BbghBackendApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    ninetyMinutesCrawlerController.run90minutesCrawler();
  }
}
