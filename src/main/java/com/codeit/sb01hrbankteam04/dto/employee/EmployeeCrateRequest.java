package com.codeit.sb01hrbankteam04.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmployeeCrateRequest {
  private String name;
  private String email;
  private Integer departmentId;
  private String position;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private String hireDate;

  private String status;
}
