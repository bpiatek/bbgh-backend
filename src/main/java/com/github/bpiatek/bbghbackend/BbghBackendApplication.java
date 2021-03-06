package com.github.bpiatek.bbghbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Clock;

@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication
public class BbghBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(BbghBackendApplication.class, args);
    System.out.println("APPLICATION STARTED");
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }
}
