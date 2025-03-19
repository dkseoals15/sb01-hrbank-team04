package com.codeit.sb01hrbankteam04.domain.backup.mapper;

import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupDto;
import com.codeit.sb01hrbankteam04.domain.backup.entity.Backup;
import com.codeit.sb01hrbankteam04.domain.backup.entity.BackupStatus;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import java.time.Instant;
import org.springframework.stereotype.Component;

/**
 * 백업 작업 관련 DTO와 Entity를 연결하는 Mapper 클래스
 */
@Component
public class BackupMapper {

  /**
   * Backup 파일을 BackupDto로 변환
   *
   * @param backup {@link Backup} 엔티티
   * @return BackupDto
   */
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

  /**
   * BackupDto와 backupFile을 통해 Backup 엔티티로 변환
   *
   * @param backupDto  {@link BackupDto} 백업 DTO
   * @param backupFile {@link File} 백업 파일
   * @return Backup 엔티티
   */
  public Backup toEntity(BackupDto backupDto, File backupFile) {
    return new Backup(backupDto.worker(), backupDto.startedAt(), backupDto.endedAt(),
        backupDto.status(), backupFile);
  }

  /**
   * workerIP와 backupFile을 통해 Backup 엔티티로 변환</br> 자동 백업 (배치 작업) 할 때 사용
   *
   * @param workerIp   업무 진행자
   * @param backupFile {@link File} 백업 파일
   * @return Backup 엔티티
   */
  public Backup toEntity(String workerIp, File backupFile) {
    return new Backup(workerIp, Instant.now(), null, BackupStatus.IN_PROGRESS, backupFile);
  }

}
