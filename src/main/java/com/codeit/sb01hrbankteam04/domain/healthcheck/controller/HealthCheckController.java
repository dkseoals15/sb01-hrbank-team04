package com.codeit.sb01hrbankteam04.domain.healthcheck.controller;

import com.codeit.sb01hrbankteam04.global.response.CustomApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/health-check")
@RestController
public class HealthCheckController implements HealthCheckControllerDoc {

  // 테스트용 Controller입니다.
  @GetMapping
  public CustomApiResponse<String> healthCheck() {
    return CustomApiResponse.ok("I'm Healthy!");
  }
}
