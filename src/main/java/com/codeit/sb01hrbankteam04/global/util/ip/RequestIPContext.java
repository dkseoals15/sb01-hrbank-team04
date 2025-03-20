package com.codeit.sb01hrbankteam04.global.util.ip;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * 현재 HTTP 요청의 클라이언트 IP 주소를 저장하는 컨텍스트 클래스 - 요청 범위(@RequestScope)로 관리되어 각 요청마다 새로운 인스턴스가 생성됨 -
 * HttpServletRequest를 통해 클라이언트 IP를 추출하여 저장
 *
 * @RequestScope 요청 단위로 빈을 생성하여 매 요청마다 새로운 IP 정보 유지
 */
@RequestScope
@Component
@Getter
public class RequestIPContext {

  private final String clientIp;

  public RequestIPContext(HttpServletRequest request) {
    this.clientIp = IPUtils.getClientIp(request);
  }
}
