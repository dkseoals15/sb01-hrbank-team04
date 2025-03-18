package com.codeit.sb01hrbankteam04.domain.backup.mapper;

import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupDto;
import com.codeit.sb01hrbankteam04.domain.backup.entity.Backup;
import com.codeit.sb01hrbankteam04.domain.backup.entity.BackupStatus;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class BackupMapper {

  public BackupDto toDto(Backup backup) {
    return BackupDto.builder()
        .id(backup.getId())
        .worker(backup.getBackupBy())
        .startedAt(backup.getStartedAt())
        .endedAt(backup.getEndedAt())
        .status(backup.getBackupStatus())
        .fileId(backup.getBackupFile() != null ? backup.getBackupFile().getId() : null)
        .build();
  }

  public Backup toEntity(BackupDto backupDto, File backupFile) {
    return new Backup(backupDto.worker(), backupDto.startedAt(), backupDto.endedAt(),
        backupDto.status(), backupFile);
  }

  public Backup toEntity(String workerIp, File backupFile) {
    return new Backup(workerIp, Instant.now(), null, BackupStatus.IN_PROGRESS, backupFile);
  }

}
