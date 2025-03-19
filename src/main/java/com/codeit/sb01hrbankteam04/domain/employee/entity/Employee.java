package com.codeit.sb01hrbankteam04.domain.employee.entity;

import com.codeit.sb01hrbankteam04.domain.department.Department;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import com.codeit.sb01hrbankteam04.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "employee")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee extends BaseEntity {

  @Column(nullable = false, length = 20)
  @Enumerated(EnumType.STRING) // TODO: 추후 수정요구해야 할 듯?
  private EmployeeStatusType status;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, unique = true, length = 70)
  private String email;

  @Column(nullable = false, unique = true, length = 30)
  private String code;

  @ManyToOne
  @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_department"))
  @OnDelete(action= OnDeleteAction.SET_NULL)
  private Department department;

  @Column(nullable = false, length = 50)
  private String position;

  @Column(nullable = false)
  private Instant joinedAt;

  @OneToOne
  @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "fk_profile"))
  @OnDelete(action= OnDeleteAction.SET_NULL)
  private File profile;

  public Employee(EmployeeStatusType status, String name,String email, String code,
      Department department, String position,Instant joinedAt, File profile) {
    this.status = status;
    this.name = name;
    this.email = email;
    this.code = code;
    this.department = department;
    this.position = position;
    this.joinedAt = joinedAt;
    this.profile = profile;
  }

}
