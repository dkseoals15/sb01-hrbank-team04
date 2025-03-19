package com.codeit.sb01hrbankteam04.domain.storage;

import com.codeit.sb01hrbankteam04.domain.file.dto.FileDto;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import org.springframework.http.ResponseEntity;

/**
 * 파일 저장 관련 interface.
 * <p>
 * 실제 파일 저장 로직 담당 인터페이스
 */
public interface FileStorage {

  /**
   * 파일을 로컬에 저장.
   *
   * @param file  파일 객체
   * @param bytes byte 파일
   * @return
   */
  File put(File file, byte[] bytes);

  /**
   * 파일 다운로드 응답 데이터 전달
   *
   * @param fileDto 다운로드하고자 하는 파일 DTO
   * @return 파일 다운로드 응답 데이텅
   */
  ResponseEntity<?> download(FileDto fileDto);

  /**
   * 저장소의 파일 삭제
   *
   * @param file 삭제하고자 하는 파일
   */
  void delete(File file);
}
