package com.codeit.sb01hrbankteam04.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Custom Error 지정 필요성이 있다면 에러 코드를 작성하여 진행.
 * <p>
 * 일반적인 상황의 에러만 적혀있지만, 특정 도메인에서 발생할 수 있는 에러 등 정의\n\n
 * CustomException(ErrorCode.INTERNAL_SERVER_ERROR) 처럼 사용.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // COMMON
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다.", "관리자에게 연락해 주세요."),
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "잘못된 요청을 진행하였습니다."),

  // FILE
  FILE_STORAGE_INIT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장소 초기화 실패",
      "파일 저장 디렉토리를 생성하는 중 오류가 발생했습니다."),
  FILE_WRITE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 실패", "파일을 저장하는 중 오류가 발생했습니다."),
  FILE_READ_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 읽기 실패", "파일을 읽는 중 오류가 발생했습니다."),
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.", "요청한 파일이 존재하지 않습니다."),
  FILE_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "파일 삭제 권한 없음", "해당 파일을 삭제할 권한이 없습니다."),
  FILE_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 실패", "파일 삭제 중 시스템 오류가 발생했습니다."),
  FILE_DOWNLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드 실패", "파일 다운로드 중 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String message;
  private final String detail;
}
