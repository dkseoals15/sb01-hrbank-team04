package com.codeit.sb01hrbankteam04.domain.employeehistory.entity;

import com.codeit.sb01hrbankteam04.domain.employeehistory.audit.EmployeeRevisionListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

/**
 * 직원의 변경 이력을 저장하는 엔티티 Hibernate Envers를 활용하여 리비전 정보를 관리함 커스텀하지 않고 기본 제공되는 revinfo 사용시 memo,
 * ipAddress 정보를 추가할 수 없어서 아래와 같이 커스텀을 진행
 */

@Setter
@Getter
@Entity
@RevisionEntity(EmployeeRevisionListener.class)
@Table(name = "EmployeeHistory")
public class EmployeeHistory {

  @Id
  @Column
  @RevisionNumber
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long revisionId;

  @Column(name = "created_at")
  @RevisionTimestamp
  private Instant at; // 엔티티 변경 시간

  @Column
  private String memo;

  //작업자 ip 주소
  @Column(name = "modified_by")
  private String ipAddress;
}