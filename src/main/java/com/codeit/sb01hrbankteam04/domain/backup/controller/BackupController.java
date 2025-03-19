package com.codeit.sb01hrbankteam04.domain.backup.controller;

import com.codeit.sb01hrbankteam04.domain.backup.docs.BackupControllerDocs;
import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupRequestDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.CursorPageResponseBackupDto;
import com.codeit.sb01hrbankteam04.domain.backup.service.BackupService;
import com.codeit.sb01hrbankteam04.global.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 파일 관련 요청을 처리하는 컨트롤러.
 * <p>
 * 백업 파일 생성, 백업 리스트 조회, 최신 백업 정보 조회
 */
@RestController
@RequestMapping("/api/backups")
@RequiredArgsConstructor
public class BackupController implements BackupControllerDocs {

  private final BackupService backupService;

  /**
   * 백업 파일 생성
   *
   * @param request {@link HttpServletRequest} - 입력 시에는 아무 것도 입력하지 않아도 됨
   * @return 생성된 백업 파일
   */
  @PostMapping
  public CustomApiResponse<BackupDto> create(HttpServletRequest request) {
    return CustomApiResponse.created(backupService.create(request));
  }

  /**
   * 백업 리스트 조회
   *
   * @param backupRequestDto {@link BackupRequestDto}
   * @return 커서 데이터를 포함한 백업 정보 리스트
   */
  @GetMapping
  public CustomApiResponse<CursorPageResponseBackupDto<BackupDto>> getBackupList(
      @ModelAttribute BackupRequestDto backupRequestDto) {
    return CustomApiResponse.ok(backupService.getBackupListWithCursor(backupRequestDto));
  }

  /**
   * 가장 최근 백업 정보 조회 (요청한 상태의 최근 백업 정보를 조회한다)
   *
   * @param status {@link String} 으로 상태를 받는다
   * @return 백업 정보 리스트
   */
  @GetMapping("/latest")
  public CustomApiResponse<BackupDto> getLatestBackup(
      @RequestParam(defaultValue = "COMPLETED") String status) {
    return CustomApiResponse.ok(backupService.getLatestBackup(status));
  }
}
