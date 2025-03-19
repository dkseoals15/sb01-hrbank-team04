package com.codeit.sb01hrbankteam04.domain.employeehistory.controller;

import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request.ChangeLogRequest;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request.FilterRequest;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.CursorPageResponseEmployeeDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.DiffDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.repository.EmployeeChangeLogRepository;
import com.codeit.sb01hrbankteam04.domain.employeehistory.type.ModifyType;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
public class EmployeeHistoryController {

  private final EntityManager entityManager;

  private final EmployeeChangeLogRepository repository;

  @GetMapping
  public ResponseEntity<CursorPageResponseEmployeeDto> getChangeLogs(
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) ModifyType type,
      @RequestParam(required = false) String memo,
      @RequestParam(required = false) String ipAddress,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Instant atFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Instant atTo,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String  cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "createdAt") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection) {

    ChangeLogRequest request = new ChangeLogRequest(idAfter, size, employeeNumber, type, memo, cursor,
        ipAddress, atFrom, atTo, sortField, sortDirection);

    CursorPageResponseEmployeeDto response =  repository.findChangeLogs(request);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}/diffs")
  public ResponseEntity<List<DiffDto>> getRevisionDetails(@PathVariable("id") Long id) {
    List<DiffDto> diffs = repository.getRevisionDetails(id);
    return ResponseEntity.ok(diffs);
  }

  @GetMapping("/count")
  public ResponseEntity<Long> getChangeLogCount(
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) ModifyType type,
      @RequestParam(required = false) String memo,
      @RequestParam(required = false) String ipAddress,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Instant atFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Instant atTo
  ) {

    Instant now = Instant.now();
    if (atFrom == null && atTo == null) {
      atFrom = now.minus(7, ChronoUnit.DAYS);
      atTo = now;
    }

    FilterRequest filterRequest = new FilterRequest(employeeNumber, type, memo, ipAddress, atFrom, atTo);

    Long count = repository.countChangeLogs(filterRequest);

    return ResponseEntity.ok(count);
  }
}
