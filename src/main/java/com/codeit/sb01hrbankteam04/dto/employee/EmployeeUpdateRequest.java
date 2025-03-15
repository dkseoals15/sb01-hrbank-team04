package com.codeit.sb01hrbankteam04.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmployeeUpdateRequest {
  private String name;
  private String email;
  private Integer departmentId;
  private String position;
  private String hireDate;
  private String status;
  private String memo;

}
