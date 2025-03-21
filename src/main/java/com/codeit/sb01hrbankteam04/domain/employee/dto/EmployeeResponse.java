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


  public EmployeeResponse(Long id, String name, String email, String employeeNumber,
      Long departmentId, String departmentName, String position,
      Instant hireDate, String statusValue, Long profileImageId) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.employeeNumber = employeeNumber;
    this.departmentId = departmentId;
    this.departmentName = departmentName;
    this.position = position;
    this.hireDate = formatHireDate(hireDate); // 날짜 포맷 변화 Instant-> String "yyyy-MM-dd"
    this.status = statusValue;
    this.profileImageId = profileImageId;
  }


  // Instant → yyyy-MM-dd 문자열 변환하는 메서드
  private String formatHireDate(Instant hireDate) {
    if (hireDate == null) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.systemDefault()); // 시스템 시간대 기준
    return formatter.format(hireDate);
  }


}
