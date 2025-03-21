package com.codeit.sb01hrbankteam04.domain.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

//직원 전체 조회 request dto
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmployeePageRequest {

  private String nameOrEmail;
  private String employeeNumber;
  private String departmentName;
  private String position;
  private String hireDateFrom;
  private String hireDateTo;
  private String status;
  private Long idAfter;
  private String cursor;
  private int size = 10;  // default
  private String sortField = "name";
  private String sortDirection = "asc";

}
