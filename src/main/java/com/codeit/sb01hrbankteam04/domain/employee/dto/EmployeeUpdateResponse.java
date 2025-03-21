package com.codeit.sb01hrbankteam04.domain.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmployeeUpdateResponse {

  private Long id;
  private String name;
  private String email;
  private String employeeNumber;
  private Integer departmentId;
  private String departmentName;
  private String position;
  private String hireDate;
  private String status;
  private Integer profileImageId;

}
