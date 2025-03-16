package com.codeit.sb01hrbankteam04.controller;

import com.codeit.sb01hrbankteam04.model.Employee;
import com.codeit.sb01hrbankteam04.model.Status;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeCrateRequest;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeResponse;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeUpdateRequest;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeUpdateResponse;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeesResponse;
import com.codeit.sb01hrbankteam04.service.EmployeeService;
import java.io.IOException;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  @GetMapping
  public ResponseEntity<EmployeesResponse> getEmployees(
      @RequestParam(required = false) String nameOrEmail,
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) String departmentName,
      @RequestParam(required = false) String position,
      @RequestParam(required = false) String hireDateFrom,
      @RequestParam(required = false) String hireDateTo,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "name") String sortField,
      @RequestParam(defaultValue = "asc") String sortDirection) {

    /*EmployeesResponse response = employeeService.getEmployeeList(
        nameOrEmail, employeeNumber, departmentName, position,
        hireDateFrom, hireDateTo, status, idAfter, cursor,
        size, sortField, sortDirection);*/
    return ResponseEntity.ok(new EmployeesResponse());
  }

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<EmployeeResponse> createEmployee(
      @RequestPart("employee") EmployeeCrateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {

    /*// 프로필 이미지 저장 (선택 사항)
    Long profileImageId = null;
    if (profile != null && !profile.isEmpty()) {
      profileImageId = employeeService.saveProfileImage(profile);
    }

    // 직원 등록
    Employee employee = employeeService.createEmployee(request, profileImageId);

    // 응답 생성
    EmployeeResponse response = EmployeeResponse.fromEntity(employee);*/
    return ResponseEntity.ok(new EmployeeResponse());
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
    /*Employee employee = employeeService.getEmployeeById(id);
    if (employee == null) {
      return ResponseEntity.notFound().build();
    }

    EmployeeResponse response = new EmployeeResponse(employee);*/
    return ResponseEntity.ok(new EmployeeResponse());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<EmployeeUpdateResponse> updateEmployee(
      @PathVariable Long id,
      @RequestBody EmployeeUpdateRequest request,
      @RequestParam(required = false) MultipartFile profile) throws IOException {

    /*Employee employee = employeeService.getEmployeeById(id);
    if (employee == null) {
      return ResponseEntity.notFound().build();
    }

    if (name != null) employee.setName(name);
    if (position != null) employee.setPosition(position);
    if (department != null) employee.setDepartment(department);

    if (profile != null && !profile.isEmpty()) {
      String filePath = employeeService.saveProfileImage(profile);
      employee.setProfileImagePath(filePath);
    }

    employeeService.updateEmployee(employee);*/
    return ResponseEntity.ok(new EmployeeUpdateResponse());
  }

  @GetMapping("/count")//총 직원 수
  public ResponseEntity<Integer> getEmployeeCount(
      @RequestParam(value = "status", required = false) Status status,
      @RequestParam(value = "fromDate", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam(value = "toDate", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
    if (toDate == null) {
      toDate = LocalDate.now(); // 기본값: 현재 날짜
    }
    return employeeService.getEmployeeCount(status, fromDate, toDate);
  }

  @GetMapping("/stats/distribution") //부서별 직원 분포,직무별 직원 분포
  public void getEmployeeDistribution(
      @RequestParam(value = "groupBy", defaultValue = "department") String groupBy,
      @RequestParam(value = "status", defaultValue = "ACTIVE") String status) {
    //ResponseEmployeeDistribution 로 반환
    //return employeeService.getEmployeeDistribution(groupBy, status);
  }

  @GetMapping("/stats/trend") //최근 1년 월별 직원수 변동 추이, 이번달 입사자 수
  public void getEmployeeTrend(
      @RequestParam(value = "from", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(value = "to", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(value = "unit", defaultValue = "month") String unit) {

    if (to == null) {
      to = LocalDate.now(); // 기본값: 현재 날짜
    }

    if (from == null) {
      from = calculateDefaultFromDate(to, unit); // 기본값: 12개 이전
    }

    //return employeeService.getEmployeeTrend(from, to, unit);
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

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    /*Employee employee = employeeService.getEmployeeById(id);
    if (employee == null) {
      return ResponseEntity.notFound().build(); // 404 Not Found 반환
    }

    employeeService.deleteEmployee(id);*/
    return ResponseEntity.noContent().build();
  }

}
