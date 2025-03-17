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

    return request.getRemoteAddr();
  }

  private static String getFirstIp(String ipList) {
    return ipList.contains(",") ? ipList.split(",")[0].trim() : ipList;
  }
}

