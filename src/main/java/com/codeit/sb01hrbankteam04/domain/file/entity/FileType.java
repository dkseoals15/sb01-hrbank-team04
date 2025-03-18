package com.codeit.sb01hrbankteam04.domain.file.entity;

import lombok.Getter;

/**
 * FileType Enum.
 * <p>
 * 어떤 파일이냐에 따라 확장자와, 파일 명을 따로 지정하고자 Enum을 통해 정의.
 * <p>
 * * 프로필 이미지(profile)의 경우, 다른 확장자 (jpg) 등도 사용 가능
 */
@Getter
public enum FileType {
  EMPLOYEE_BACKUP("employee_backup_%d_%s", ".csv"),
  ERROR_LOG("error_log_%d_%s", ".log"),
  PROFILE("profile_%d_%s", ".png");

  private final String nameFormat;
  private final String defaultExtension;

  FileType(String nameFormat, String defaultExtension) {
    this.nameFormat = nameFormat;
    this.defaultExtension = defaultExtension;
  }
}
