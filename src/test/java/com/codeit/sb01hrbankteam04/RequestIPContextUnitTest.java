package com.codeit.sb01hrbankteam04;

import com.codeit.sb01hrbankteam04.global.util.ip.RequestIPContext;
import jakarta.servlet.http.HttpServletRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RequestIPContextUnitTest {

  @Mock
  private HttpServletRequest request;

  @Test
  public void testClientIp_fromXForwardedFor() {
    Mockito.when(request.getHeader("X-Forwarded-For")).thenReturn("210.117.67.8");
    RequestIPContext requestIPContext = new RequestIPContext(request);
    assertEquals("210.117.67.8", requestIPContext.getClientIp());
  }

  @Test
  public void testClientIp_fromRemoteAddr() {
    Mockito.when(request.getRemoteAddr()).thenReturn("127.0.0.1");
    RequestIPContext requestIPContext = new RequestIPContext(request);
    assertEquals("127.0.0.1", requestIPContext.getClientIp());
  }
}

