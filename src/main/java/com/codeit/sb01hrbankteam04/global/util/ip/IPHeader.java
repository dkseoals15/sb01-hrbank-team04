package com.codeit.sb01hrbankteam04.global.util.ip;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

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