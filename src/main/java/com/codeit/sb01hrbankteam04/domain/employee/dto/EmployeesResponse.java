package com.codeit.sb01hrbankteam04.domain.employee.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmployeesResponse {

  private List<EmployeeResponse> content;
  private String nextCursor;
  private Long nextIdAfter;
  private int size;
  private long totalElements;
  private boolean hasNext;
}
