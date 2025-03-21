package com.codeit.sb01hrbankteam04.domain.employee.entity;

import com.codeit.sb01hrbankteam04.domain.department.entity.Department;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import com.codeit.sb01hrbankteam04.global.entity.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Random;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Setter // TODO: 일시적으로 추가!!
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

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "fk_profile"), nullable = true)
  @OnDelete(action = OnDeleteAction.SET_NULL)
  @NotAudited // 프로필은 추적하지 않음
  private File profile;

  @Builder
  public Employee(EmployeeStatusType status, String name, String email,
      Department department, String position, Instant joinedAt, File profile) {
    this.status = status;
    this.name = name;
    this.email = email;
    this.code = makeCode(joinedAt);
    this.department = department;
    this.position = position;
    this.joinedAt = joinedAt;
    this.profile = profile;
  }


  public void update(String name, String email, Department department, String position,
      Instant joinedAt, EmployeeStatusType status, File profile) {
    this.name = name;
    this.email = email;
    this.department = department;
    this.position = position;
    this.joinedAt = joinedAt;
    this.status = status;
    this.profile = profile;
  }

  @SuppressWarnings("checkstyle:WhitespaceAround")
  public String makeCode(Instant joinedAt) {
    return "EMP-" + joinedAt.toString().substring(0, 4)
        + "-" + String.format("%013d", new Random().nextInt(999999999));
  }

}
