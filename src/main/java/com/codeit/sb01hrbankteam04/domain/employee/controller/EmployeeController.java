package com.codeit.sb01hrbankteam04.domain.employee.controller;

import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeDistributionResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeePageResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeTrendResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeUpdateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.domain.employee.service.EmployeeService;
import com.codeit.sb01hrbankteam04.domain.employeehistory.audit.EmployeeRevisionListener;
import com.codeit.sb01hrbankteam04.global.util.ip.RequestIPContext;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.management.InstanceAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

  private final EmployeeService employeeService;

  private final RequestIPContext requestIPContext;

  // 직원 CRUD
  @GetMapping
  public ResponseEntity<EmployeePageResponse> getEmployees(
      @RequestParam(required = false) String nameOrEmail,
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) String departmentName,
      @RequestParam(required = false) String position,
      @RequestParam(required = false) String hireDateFrom,
      @RequestParam(required = false) String hireDateTo,
      @RequestParam(required = false) String status,
      @RequestParam(name ="idAfter", required = false) Long nextIdAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "hireDate") String sortField,
      @RequestParam(defaultValue = "asc") String sortDirection) {

    return ResponseEntity.ok(employeeService.getEmployees(
        nameOrEmail, employeeNumber, departmentName, position, hireDateFrom, hireDateTo, status,
        nextIdAfter, cursor, size, sortField, sortDirection));
  }

  //직원 등록
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeResponse> createEmployee(
      @RequestPart("employee") EmployeeCreateRequest employeeCreateRequest,
      @RequestPart(value = "profile", required = false) MultipartFile profile
  ) throws InstanceAlreadyExistsException, IOException {

    EmployeeRevisionListener.setMemo(employeeCreateRequest.memo());
    EmployeeRevisionListener.setModified(requestIPContext.getClientIp());

    EmployeeResponse createdEmployee = employeeService.create(employeeCreateRequest,
        profile);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdEmployee);
  }

  //직원 상세 조회
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeResponse> find(@PathVariable Long id) {
    EmployeeResponse employee = employeeService.find(id);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(employee);
  }

  //직원 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {

    EmployeeRevisionListener.setMemo("직원 삭제");
    EmployeeRevisionListener.setModified(requestIPContext.getClientIp());

    employeeService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  //직원 수정
  @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeResponse> update(
      @PathVariable("id") Long id,
      @RequestPart(name = "employee", required = true) EmployeeUpdateRequest updateRequest,
      @RequestPart(name = "profile", required = false) MultipartFile profile)
      throws IOException {

    EmployeeRevisionListener.setMemo(updateRequest.memo());
    EmployeeRevisionListener.setModified(requestIPContext.getClientIp());

    //TODO: update를 엔티티에서 하도록하기?
    EmployeeResponse employeeResponse = employeeService.update(id, updateRequest, profile);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(employeeResponse);

  }

  // Dashboard

  //총 직원 수
  @GetMapping("/count")
  public ResponseEntity<Integer> getEmployeeCount(
      @RequestParam(value = "status", required = false) EmployeeStatusType status,
      @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

    if (toDate == null) {
      toDate = LocalDate.now(); // 기본 값: 현재 날짜
    }
    return employeeService.getEmployeeCount(status, fromDate, toDate);
  }

  //부서별 직원 분포,직무별 직원 분포
  @GetMapping("/stats/distribution")
  public ResponseEntity<List<EmployeeDistributionResponse>> getEmployeeDistribution(
      @RequestParam(value = "groupBy", defaultValue = "department") String groupBy,
      @RequestParam(value = "status", defaultValue = "ACTIVE") EmployeeStatusType status) {
    List<EmployeeDistributionResponse> employeeDistribution = employeeService.getEmployeeDistribution(
        groupBy, status);
    return ResponseEntity.ok(employeeDistribution);
  }

  //최근 1년 월별 직원수 변동 추이, 이번달 입사자 수
  @GetMapping("/stats/trend")
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
