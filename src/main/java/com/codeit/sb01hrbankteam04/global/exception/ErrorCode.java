package com.codeit.sb01hrbankteam04.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // COMMON
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다.", "관리자에게 연락해 주세요."),
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "잘못된 요청을 진행하였습니다."),
  ;

  private final HttpStatus status;
  private final String message;
  private final String detail;
}
