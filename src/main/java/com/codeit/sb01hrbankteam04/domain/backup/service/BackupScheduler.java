package com.codeit.sb01hrbankteam04.domain.backup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 배치에 의한 데이터 백업 작업을 위한 스케줄러
 */
@Component
@RequiredArgsConstructor
public class BackupScheduler {

  private final BackupService backupService;

  /**
   * 특정 주기마다 백업 실행
   */
  @Scheduled(fixedRateString = "${backup.schedule.rate}")
  public void performScheduleedBackup() {
    backupService.create("system");
  }
}
