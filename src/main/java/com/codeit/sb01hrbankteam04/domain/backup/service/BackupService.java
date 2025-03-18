package com.codeit.sb01hrbankteam04.domain.backup.service;

import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupRequestDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.CursorPageResponseBackupDto;
import jakarta.servlet.http.HttpServletRequest;

public interface BackupService {

  BackupDto create(HttpServletRequest httpServletRequest);

  BackupDto create(String worker);

  CursorPageResponseBackupDto<BackupDto> getBackupListWithCursor(
      BackupRequestDto requestDto);

  BackupDto getLatestBackup(String status);
}
