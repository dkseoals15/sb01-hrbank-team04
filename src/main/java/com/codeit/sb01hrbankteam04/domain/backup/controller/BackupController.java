package com.codeit.sb01hrbankteam04.domain.backup.controller;

import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupRequestDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.CursorPageResponseBackupDto;
import com.codeit.sb01hrbankteam04.domain.backup.service.BackupService;
import com.codeit.sb01hrbankteam04.global.response.CustomApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/backups")
@RequiredArgsConstructor
public class BackupController {

  private final BackupService backupService;

  @PostMapping
  public CustomApiResponse<?> create(HttpServletRequest request) {
    return CustomApiResponse.created(backupService.create(request));
  }

  @GetMapping
  public CustomApiResponse<CursorPageResponseBackupDto<BackupDto>> getBackupList(
      @ModelAttribute BackupRequestDto backupRequestDto) {
    return CustomApiResponse.ok(backupService.getBackupListWithCursor(backupRequestDto));
  }

  @GetMapping("/latest")
  public CustomApiResponse<BackupDto> getLatestBackup(
      @RequestParam(defaultValue = "COMPLETED") String status) {
    return CustomApiResponse.ok(backupService.getLatestBackup(status));
  }
}
