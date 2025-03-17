package com.codeit.sb01hrbankteam04.global.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
@Getter
public class RequestIPContext {

  private final String clientIp;

  public RequestIPContext(HttpServletRequest request) {
    this.clientIp = IPUtils.getClientIp(request);
  }
}
