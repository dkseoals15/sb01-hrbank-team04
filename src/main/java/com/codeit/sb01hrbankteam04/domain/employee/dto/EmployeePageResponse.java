package com.codeit.sb01hrbankteam04.domain.employee.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmployeePageResponse {

  List<EmployeeResponse> content;
  String nextCursor;
  Long nextIdAfter;
  int size;
  long totalElements;
  boolean hasNext;

  public static EmployeePageResponse from(
      List<EmployeeResponse> employeeResponses,
      String nextCursor,
      Long lastId,
      int size,
      Long totalElements,
      boolean hasNext) {
    return new EmployeePageResponse(employeeResponses, nextCursor, lastId, size, totalElements,
        hasNext);

  }
}