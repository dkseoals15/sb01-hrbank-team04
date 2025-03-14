package com.codeit.sb01hrbankteam04.global.response;

import com.codeit.sb01hrbankteam04.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponse(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC") Instant timestamp,
    HttpStatus status, String message, String details) {

  public static ErrorResponse of(ErrorCode errorCode) {
    return ErrorResponse.builder()
        .timestamp(Instant.now())
        .status(errorCode.getStatus())
        .message(errorCode.getMessage())
        .details(errorCode.getDetail())
        .build();
  }

}
