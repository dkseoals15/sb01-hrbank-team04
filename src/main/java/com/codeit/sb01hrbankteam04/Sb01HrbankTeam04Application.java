package com.codeit.sb01hrbankteam04;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 스케줄링 활성화
public class Sb01HrbankTeam04Application {

  public static void main(String[] args) {
    SpringApplication.run(Sb01HrbankTeam04Application.class, args);
  }

}
