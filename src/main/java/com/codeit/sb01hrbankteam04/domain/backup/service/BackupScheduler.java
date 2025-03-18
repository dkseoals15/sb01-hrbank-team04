package com.codeit.sb01hrbankteam04.domain.backup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BackupScheduler {

  private final BackupService backupService;

  @Scheduled(fixedRateString = "${backup.schedule.rate}")
  public void performScheduleedBackup() {
    log.info("자동 백업 작업 시작 ... ");
    backupService.create("system");
    log.info("자동 백업 작업 완료.");
  }
}
