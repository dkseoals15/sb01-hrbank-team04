package com.codeit.sb01hrbankteam04.global.util.ip;

import jakarta.servlet.http.HttpServletRequest;

public class IPUtils {

  public static String getClientIp(HttpServletRequest request) {
    for (String header : IPHeader.getAllHeaderNames()) {
      String ip = request.getHeader(header);
      if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
        return getFirstIp(ip);
      }
    }
    // IPv6 localhost (::1)을 IPv4 (127.0.0.1)로 변환 - 강제 변환
    String remoteAddr = request.getRemoteAddr();
    if ("0:0:0:0:0:0:0:1".equals(remoteAddr)) {
      remoteAddr = "127.0.0.1";
    }
    return remoteAddr;
  }

  private static String getFirstIp(String ipList) {
    String[] ips = ipList.split(",");
    for (String ip : ips) {
      ip = ip.trim();
      if (isIpv4(ip)) { // IPv4가 있으면 우선 반환
        return ip;
      }
    }
    return ips[0].trim(); // IPv4가 없으면 첫 번째 값 반환 (IPv6)
  }

  private static boolean isIpv4(String ip) {
    return ip.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
  }

}

