package com.codeit.sb01hrbankteam04.domain.backup.entity;

import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import com.codeit.sb01hrbankteam04.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * 백업 엔티티</br> - 업데이트 불가능한 객체
 */
@Entity
@Table(name = "backup_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Backup extends BaseEntity {

  @Column(name = "backup_by", length = 45, nullable = false)
  private String backupBy;

  @Column(name = "started_at", nullable = false)
  private Instant startedAt;

  @Column(name = "ended_at")
  private Instant endedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private BackupStatus backupStatus;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "backup_file_id", foreignKey = @ForeignKey(name = "fk_backup_file"))
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private File backupFile;

  /**
   * 백업 객체 생성자
   */
  public Backup(String backupBy, Instant startedAt, Instant endedAt, BackupStatus backupStatus,
      File backupFile) {
    this.backupBy = backupBy;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
    this.backupStatus = backupStatus;
    this.backupFile = backupFile;
  }

  /**
   * 상태 업데이트
   */
  public void updateStatus(BackupStatus backupStatus) {
    this.backupStatus = backupStatus;
  }

  /**
   * 백업 파일 업데이트
   */
  public void updateBackupFile(File backupFile) {
    this.backupFile = backupFile;
  }

  /**
   * 작업 완료 시간 업데이트
   */
  public void updateEndedAt(Instant endedAt) {
    this.endedAt = endedAt;
  }

}
