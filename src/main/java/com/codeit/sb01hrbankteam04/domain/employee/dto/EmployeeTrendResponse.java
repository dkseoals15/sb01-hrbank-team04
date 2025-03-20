package com.codeit.sb01hrbankteam04.domain.employee.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmployeeTrendResponse {

  LocalDate date;
  int count;
  int change;
  double changeRate;

  @Override
  public String toString() {
    return "EmployeeTrendResponse{" +
        "date=" + date +
        ", count=" + count +
        ", change=" + change +
        ", changeRate=" + changeRate +
        '}';
  }
}
