package com.codeit.sb01hrbankteam04.domain.employee.dto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class EmployeeResponse {
  private Long id;
  private String name;
  private String email;
  private String employeeNumber;
  private Long departmentId;
  private String departmentName;
  private String position;
  private String hireDate;
  private String status;
  private Long profileImageId;


  // `Instant`를 받아서 `hireDate` 필드에 변환된 값 저장하는 생성자 추가
  public EmployeeResponse(Long id, String name, String email, String employeeNumber,
      Long departmentId, String departmentName, String position,
      Instant hireDate, String status, Long profileImageId) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.employeeNumber = employeeNumber;
    this.departmentId = departmentId;
    this.departmentName = departmentName;
    this.position = position;
    this.hireDate = formatHireDate(hireDate); // 날짜 포맷 변화
    this.status = status;
    this.profileImageId = profileImageId;
  }


  // Instant → yyyy-MM-dd 문자열 변환하는 메서드
  private String formatHireDate(Instant hireDate) {
    if (hireDate == null) return null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.systemDefault()); // 시스템 시간대 기준
    return formatter.format(hireDate);
  }

  public String toString() {
    return "EmployeeResponse{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", code='" + employeeNumber + '\'' +
        ", departmentId=" + departmentId +
        ", departmentName='" + departmentName + '\'' +
        ", position='" + position + '\'' +
        ", hireDate=" + hireDate +
        ", status='" + status + '\'' +
        ", profileImageId=" + profileImageId +
        '}';

    }
  }
