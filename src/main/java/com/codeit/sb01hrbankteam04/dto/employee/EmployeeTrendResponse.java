package com.codeit.sb01hrbankteam04.dto.employee;

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
  int chaange;
  double changeRate;

}
