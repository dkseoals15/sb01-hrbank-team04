package com.codeit.sb01hrbankteam04.domain.backup.entity;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BackupStatus {
  IN_PROGRESS("진행중"),
  COMPLETED("완료"),
  FAILED("실패"),
  SKIPPED("건너뜀");

  private final String kor; // TODO: 아 이름 애매해 근데 생각나는게 없엉ㅠ

  public static BackupStatus fromKor(String kor) {
    for (BackupStatus status : BackupStatus.values()) {
      if (status.kor.equals(kor)) {
        return status;
      }
    }
    throw new IllegalArgumentException("Unknown status: " + kor);
  }

  public static BackupStatus fromString(String value) {
    if (StringUtils.isBlank(value)) {
      throw new IllegalArgumentException("BackupStatus은 null이 될 수 없습니다.");
    }
    try {
      return BackupStatus.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Unknown status: " + value);
    }
  }
}
