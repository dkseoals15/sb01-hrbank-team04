package com.codeit.sb01hrbankteam04.domain.backup.repository;

import com.codeit.sb01hrbankteam04.domain.backup.entity.Backup;
import com.codeit.sb01hrbankteam04.domain.backup.entity.BackupStatus;
import java.time.Instant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 백업 테이블과 연동된 JPA 레포지토리
 */
@Repository
public interface BackupRepository extends JpaRepository<Backup, Long>,
    JpaSpecificationExecutor<Backup> {

  /**
   * 마지막에 배치 작업이 완료된 시간 반환
   *
   * @return Instant
   */
  @Query("select max (b.endedAt) from Backup b where b.backupStatus = 'COMPLETED'")
  Instant findLastCompletedBatchTime();

  /**
   * 백업 상태로 필터링하여 최신 백업 정보 리턴
   *
   * @param backupStatus 백업 상태
   */
  @EntityGraph(attributePaths = "backupFile")
  Backup findTop1ByBackupStatusOrderByEndedAtDesc(BackupStatus backupStatus);
}
