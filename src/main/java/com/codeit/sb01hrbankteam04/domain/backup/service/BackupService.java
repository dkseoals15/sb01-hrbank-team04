package com.codeit.sb01hrbankteam04.domain.backup.service;

import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupRequestDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.CursorPageResponseBackupDto;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 백업 관련 비즈니스 로직을 정의하는 서비스 인터페이스.
 * <p>
 * 백업 생성, 조회 및 페이징 처리된 백업 이력 제공 등의 기능을 포함
 */
public interface BackupService {

  /**
   * 백업 생성 (Client 요청에 의한 데이터 백업)
   *
   * @param httpServletRequest 클라이언트 IP를 가져오기 위함
   */
  BackupDto create(HttpServletRequest httpServletRequest);

  /**
   * 백업 생성 (배치에 의한 데이터 백업 시)
   *
   * @param worker 작업자 이름 (system)
   */
  BackupDto create(String worker);

  /**
   * 페이징 된 백업 이력 리스트 전달
   *
   * @param requestDto 필터링 등을 위한 요청 데이터
   * @return 페이징 된 백업 이력 목룍
   */
  CursorPageResponseBackupDto<BackupDto> getBackupListWithCursor(
      BackupRequestDto requestDto);

  /**
   * 요청한 상태의 가장 최근 백업 이력 조회
   *
   * @param status 백업 상태
   * @return 백업 이력
   */
  BackupDto getLatestBackup(String status);
}
