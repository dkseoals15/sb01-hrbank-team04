package com.codeit.sb01hrbankteam04.global.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

public class IPUtils {
  public static String getClientIp(HttpServletRequest request) {
    List<String> headers = Arrays.asList(
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR",
        "X-Real-IP",
        "X-RealIP"
    );

    // Check for the "X-Forwarded-For" header first
    for (String header : headers) {
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

