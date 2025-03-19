package com.codeit.sb01hrbankteam04.domain.employee.entity;

import com.codeit.sb01hrbankteam04.domain.department.Department;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import com.codeit.sb01hrbankteam04.global.entity.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "employee")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Audited
public class Employee extends BaseUpdatableEntity {

  @Column(nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  @Setter
  private EmployeeStatusType status;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, unique = true, length = 70)
  private String email;

  @Column(nullable = false, unique = true, length = 30)
  private String code;

  @ManyToOne
  @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_department"))
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED) // department_id 만 추적
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private Department department;

  @Column(nullable = false, length = 50)
  private String position;

  @Column(nullable = false)
  private Instant joinedAt;

  @OneToOne(optional = true)
  @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "fk_profile"), nullable = true)
  @OnDelete(action= OnDeleteAction.SET_NULL)
  @NotAudited // 프로필은 추적하지 않음
  private File profile;

  @Builder
  public Employee(EmployeeStatusType status, String name,String email,
      Department department, String position,Instant joinedAt, File profile) {
    this.status = status;
    this.name = name;
    this.email = email;
    this.code = code;
    this.department = department;
    this.position = position;
    this.joinedAt = joinedAt;
    this.profile = profile;

    this.code = makeCode(joinedAt);
  }

  public void update(String name,String email, Department department, String position,
      Instant joinedAt, EmployeeStatusType status, File profile){
    this.name = name;
    this.email = email;
    this.department = department;
    this.position = position;
    this.joinedAt = joinedAt;
    this.status = status;
    this.profile = profile;
  }

  public String makeCode(Instant joinedAt){
    return "EMP-"+joinedAt.toString().substring(0, 4)
        +"-"+String.format("%013d", new Random().nextInt(999999999));
  }

}
