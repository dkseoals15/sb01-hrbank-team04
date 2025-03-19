package com.codeit.sb01hrbankteam04.domain.department;

import com.codeit.sb01hrbankteam04.global.entity.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: 임시로 복사해 둔 것
@Entity
@Table(name = "department")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Department extends BaseUpdatableEntity {

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  @Column(nullable = false, length = 200)
  private String description;

  @Column(name = "established_at", nullable = false)
  private Instant establishedDate;

  public void updateDepartment(String name, String description, Instant establishedDate) {
    this.name = name;
    this.description = description;
    this.establishedDate = establishedDate;
  }
}