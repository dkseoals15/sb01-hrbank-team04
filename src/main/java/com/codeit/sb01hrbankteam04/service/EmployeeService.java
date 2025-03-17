package com.codeit.sb01hrbankteam04.service;

import com.codeit.sb01hrbankteam04.dto.employee.EmployeeDistributionResponse;
import com.codeit.sb01hrbankteam04.model.Status;
import com.codeit.sb01hrbankteam04.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class EmployeeService {
  private final EmployeeRepository employeeRepository;

  //상태 필터링 및 입사일 기간 필터링
  public ResponseEntity<Integer> getEmployeeCount(Status status, LocalDate fromDate, LocalDate toDate) {

    Instant fromInstant = (fromDate != null)
        ? fromDate.atStartOfDay(ZoneOffset.UTC).toInstant()
        : Instant.ofEpochMilli(0);

    Instant toInstant = (toDate != null)
        ? toDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant()
        : Instant.now();

    int count = employeeRepository.countEmployees(status, fromInstant, toInstant);
    return ResponseEntity.ok(count);
  }

  public List<EmployeeDistributionResponse> getEmployeeDistribution(String groupBy, Status status) {
    List<EmployeeDistributionResponse> distributionList;
    long totalEmployees = employeeRepository.countByStatus(status); // 전체 직원 수 조회

    if ("department".equalsIgnoreCase(groupBy)) {
      distributionList = employeeRepository.findEmployeeCountByDepartment(status)
          .stream()
          .map(result -> new EmployeeDistributionResponse(
              result.getGroupName(),
              result.getCount(),
              calculatePercentage(result.getCount(), totalEmployees)
          ))
          .collect(Collectors.toList());

    } else if ("position".equalsIgnoreCase(groupBy)) {
      distributionList = employeeRepository.findEmployeeCountByPosition(status)
          .stream()
          .map(result -> new EmployeeDistributionResponse(
              result.getGroupName(),
              result.getCount(),
              calculatePercentage(result.getCount(), totalEmployees)
          ))
          .collect(Collectors.toList());

    } else {
      throw new IllegalArgumentException("Invalid groupBy value: " + groupBy);
    }

    return distributionList;
  }

  private double calculatePercentage(long count, long total) {
    return (total == 0) ? 0 : (count * 100.0) / total;
  }
}
