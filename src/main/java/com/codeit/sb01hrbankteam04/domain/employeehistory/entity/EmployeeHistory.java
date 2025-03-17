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

  @Column
  @RevisionTimestamp
  private Instant createdAt; // 엔티티 변경 시간

  @Column
  private String memo;

  //작업자 ip 주소
  @Column
  private String modifiedBy;
}