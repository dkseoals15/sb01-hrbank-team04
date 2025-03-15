package com.codeit.sb01hrbankteam04.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmployeeDistributionResponse {
  String groupKey;
  int count;
  double percentage;

}
