package com.codeit.sb01hrbankteam04.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmployeeResponse {
  private Long id;
  private String name;
  private String email;
  private String employeeNumber;
  private Integer departmentId;
  private String departmentName;
  private String position;
  private String hireDate;
  private String status;
  private Long profileImageId;

}
