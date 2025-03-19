package com.codeit.sb01hrbankteam04.domain.employee.service;

import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.domain.employee.repository.EmployeeRepository;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeDistributionResponse;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeTrendResponse;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
  public ResponseEntity<Integer> getEmployeeCount(EmployeeStatusType status, LocalDate fromDate,
      LocalDate toDate) {

    Instant fromInstant = (fromDate != null)
        ? fromDate.atStartOfDay(ZoneOffset.UTC).toInstant()
        : Instant.ofEpochMilli(0);

    Instant toInstant = (toDate != null)
        ? toDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant()
        : Instant.now();

    int count = employeeRepository.countEmployees(status, fromInstant, toInstant);
    return ResponseEntity.ok(count);
  }

  public List<EmployeeDistributionResponse> getEmployeeDistribution(String groupBy, EmployeeStatusType status) {
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

  public List<EmployeeTrendResponse> getEmployeeTrend(LocalDate from, LocalDate to, String unit) {
    List<EmployeeTrendResponse> list = new ArrayList<>();
    LocalDate min_LocalDate = LocalDate.of(1970,1,1); //이 기준일 논의 필요?
    Instant min_Instant = min_LocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

    Instant startInstant = from.atStartOfDay().toInstant(ZoneOffset.UTC);
    int beforeCount = employeeRepository.countEmployees(EmployeeStatusType.재직중, min_Instant, startInstant);

    list.add(new EmployeeTrendResponse(from, beforeCount, 0, 0));

    while (!from.isAfter(to)) {
      LocalDate nextFrom;

      switch (unit) {
        case "day":
          nextFrom = from.plusDays(1);
          break;
        case "week":
          nextFrom = from.plusWeeks(1);
          break;
        case "quarter":
          nextFrom = from.plusMonths(3);
          break;
        case "year":
          nextFrom = from.plusYears(1);
          break;
        case "month":
        default:
          nextFrom = from.plusMonths(1);
          break;
      }

      Instant nextInstant = nextFrom.atStartOfDay(ZoneId.systemDefault()).toInstant();
      Instant currentInstant = from.atStartOfDay(ZoneId.systemDefault()).toInstant();

      Integer activeCount = employeeRepository.countEmployees(EmployeeStatusType.재직중, min_Instant, nextInstant);
      Integer onLeaveCount = employeeRepository.countEmployees(EmployeeStatusType.휴직중, min_Instant, nextInstant);

      int count = (activeCount != null ? activeCount : 0) + (onLeaveCount != null ? onLeaveCount : 0);
      int change = count - beforeCount;
      double percentage = calculatePercentage(count, change, beforeCount);

      list.add(new EmployeeTrendResponse(nextFrom, Math.abs(count), change, percentage));

      beforeCount = count;
      from = nextFrom;
    }
    list.remove(list.size() - 1);

    return list;
  }

  private double calculatePercentage(int count, int change, int beforeCount) {
    if (beforeCount == 0) {
      return count > 0 ? 100.0 : 0.0;
    }
    return ((double) change / beforeCount) * 100.0;
  }


}
