package com.codeit.sb01hrbankteam04.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;

/**
 * 엔티티 작성 시 공통적으로 들어가는 값들에 대해 정의
 * <p>
 * ID 생성 및 생성 일시를 매핑해 줌
 */
@MappedSuperclass
@Getter
public abstract class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
  @NotAudited
  private Instant createdAt;

}
