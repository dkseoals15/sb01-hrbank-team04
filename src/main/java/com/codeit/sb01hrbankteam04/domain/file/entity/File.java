package com.codeit.sb01hrbankteam04.domain.file.entity;

import com.codeit.sb01hrbankteam04.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 파일 엔티티. 생성만 가능, 갱신 불가능.
 */
@Entity
@Table(name = "file")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseEntity {

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "content_type", nullable = false, length = 100)
  private String contentType;

  @Column(name = "size", nullable = false)
  private Long size;

  /**
   * 파일 생성자.
   *
   * @param name        파일명
   * @param contentType 파일 타입
   * @param size        파일 크기
   */
  public File(String name, String contentType, Long size) {
    this.name = name;
    this.contentType = contentType;
    this.size = size;
  }

  /**
   * 파일 명 업데이트
   *
   * @param name 업데이트 할 파일 이름
   */
  public void updateName(String name) {
    this.name = name;
  }

}
