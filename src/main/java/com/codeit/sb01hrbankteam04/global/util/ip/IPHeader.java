package com.codeit.sb01hrbankteam04.global.util.ip;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

/**
 * HTTP 요청에서 클라이언트 IP를 식별하기 위해 확인할 수 있는 헤더 목록을 정의하는 열거형(Enum) - 프록시나 로드 밸런서를 거친 요청에서도 올바른 클라이언트 IP를
 * 가져오기 위해 다양한 헤더를 확인함
 */
@RequiredArgsConstructor
public enum IPHeader {
  X_FORWARDED_FOR("X-Forwarded-For"),
  PROXY_CLIENT_IP("Proxy-Client-IP"),
  WL_PROXY_CLIENT_IP("WL-Proxy-Client-IP"),
  HTTP_CLIENT_IP("HTTP_CLIENT_IP"),
  HTTP_X_FORWARDED_FOR("HTTP_X_FORWARDED_FOR"),
  X_REAL_IP("X-Real-IP"),
  X_REALIP("X-RealIP");

  private final String headerName;

  public String getHeaderName() {
    return headerName;
  }

  public static List<String> getAllHeaderNames() {
    return Arrays.stream(values())
        .map(IPHeader::getHeaderName)
        .collect(Collectors.toList());
  }
}