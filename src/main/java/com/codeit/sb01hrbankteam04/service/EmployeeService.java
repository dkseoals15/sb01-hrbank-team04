package com.codeit.sb01hrbankteam04.service;

import com.codeit.sb01hrbankteam04.dto.employee.EmployeeCrateRequest;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeesResponse;
import com.codeit.sb01hrbankteam04.model.Employee;
import com.codeit.sb01hrbankteam04.model.Status;
import com.codeit.sb01hrbankteam04.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Transactional
public class EmployeeService {
  private final EmployeeRepository employeeRepository;

  //상태 필터링 및 입사일 기간 필터링
  public ResponseEntity<Integer> getEmployeeCount(Status status, LocalDate fromDate, LocalDate toDate) {

    Instant fromInstant = (fromDate != null)
        ? fromDate.atStartOfDay(ZoneOffset.UTC).toInstant()
        : Instant.ofEpochMilli(0); // Unix epoch time (1970-01-01T00:00:00Z)

    Instant toInstant = (toDate != null)
        ? toDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant()
        : Instant.now();

    int count = employeeRepository.countEmployees(status, fromInstant, toInstant);
    return ResponseEntity.ok(count);
  }
}
