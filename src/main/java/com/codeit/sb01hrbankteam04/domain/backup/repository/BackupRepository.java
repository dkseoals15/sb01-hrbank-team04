package com.codeit.sb01hrbankteam04.domain.backup.repository;

import com.codeit.sb01hrbankteam04.domain.backup.entity.Backup;
import com.codeit.sb01hrbankteam04.domain.backup.entity.BackupStatus;
import java.time.Instant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupRepository extends JpaRepository<Backup, Long>,
    JpaSpecificationExecutor<Backup> {

  @Query("select max (b.endedAt) from Backup b where b.backupStatus = 'COMPLETED'")
  Instant findLastCompletedBatchTime();

  @EntityGraph(attributePaths = "backupFile")
  Backup findTop1ByBackupStatusOrderByEndedAtDesc(BackupStatus backupStatus);
}
