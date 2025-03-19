package com.codeit.sb01hrbankteam04.domain.employee.controller;

import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeDistributionResponse;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeTrendResponse;
import com.codeit.sb01hrbankteam04.domain.employee.service.EmployeeService;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

  private final EmployeeService employeeService;

  @GetMapping("/count")//총 직원 수
  public ResponseEntity<Integer> getEmployeeCount(
      @RequestParam(value = "status", required = false) EmployeeStatusType status,
      @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
    
    if (toDate == null) {
      toDate = LocalDate.now(); // 기본 값: 현재 날짜
    }
    return employeeService.getEmployeeCount(status, fromDate, toDate);
  }

  @GetMapping("/stats/distribution") //부서별 직원 분포,직무별 직원 분포
  public ResponseEntity<List<EmployeeDistributionResponse>> getEmployeeDistribution(
      @RequestParam(value = "groupBy", defaultValue = "department") String groupBy,
      @RequestParam(value = "status", defaultValue = "ACTIVE") EmployeeStatusType status) {
    System.out.println(" EmployeeStatusType = " + status);
    List<EmployeeDistributionResponse> employeeDistribution = employeeService.getEmployeeDistribution(
        groupBy, status);
    return ResponseEntity.ok(employeeDistribution);
  }

  @GetMapping("/stats/trend") //최근 1년 월별 직원수 변동 추이, 이번달 입사자 수
  public ResponseEntity<List<EmployeeTrendResponse>> getEmployeeTrend(
      @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(value = "unit", defaultValue = "month") String unit) {

    if (to == null) {
      to = LocalDate.now(); // 기본값: 현재 날짜
    }
    if (from == null) {
      from = calculateDefaultFromDate(to, unit); // 기본값: 12개 이전
    }

    List<EmployeeTrendResponse> employeeTrend = employeeService.getEmployeeTrend(from, to, unit);
    return ResponseEntity.ok(employeeTrend);
  }

  private LocalDate calculateDefaultFromDate(LocalDate to, String unit) {
    switch (unit) {
      case "day":
        return to.minusDays(12);
      case "week":
        return to.minusWeeks(12);
      case "quarter":
        return to.minusMonths(12 * 3);
      case "year":
        return to.minusYears(12);
      case "month":
      default:
        return to.minusMonths(12);
    }
  }
}
