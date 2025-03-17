package com.codeit.sb01hrbankteam04.global.util;

import jakarta.servlet.http.HttpServletRequest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RequestIPContextTest {

  @Autowired
  private RequestIPContext requestIPContext;

  // 테스트: getRemoteAddr()에서 IP 주소가 "127.0.0.1"인 경우
  @Test
  public void testClientIp_fromRemoteAddr() {
    // 로컬 IP가 "127.0.0.1"로 반환되는지 확인
    assertEquals("127.0.0.1", requestIPContext.getClientIp());
  }

  // 테스트: X-Forwarded-For 헤더에 IP가 있을 때
  @Test
  public void testClientIp_fromXForwardedFor() {
    // HttpServletRequest 모의 객체 생성
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

    // "X-Forwarded-For" 헤더에 "210.117.67.8" IP를 설정
    Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("210.117.67.8");

    RequestIPContext requestIPContext = new RequestIPContext(request);

    // 클라이언트 IP가 "210.117.67.8"로 반환되는지 확인
    assertEquals("210.117.67.8", requestIPContext.getClientIp());
  }
}
