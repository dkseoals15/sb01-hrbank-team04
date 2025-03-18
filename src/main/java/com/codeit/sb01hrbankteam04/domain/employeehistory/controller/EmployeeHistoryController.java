package com.codeit.sb01hrbankteam04.domain.employeehistory.controller;

import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request.ChangeLogRequest;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.CursorPageResponseEmployeeDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.DiffDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.repository.EmployeeChangeLogRepository;
import com.codeit.sb01hrbankteam04.domain.employeehistory.type.ModifyType;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.SortHandlerMethodArgumentResolverCustomizer;
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

  private final EmployeeChangeLogRepository employeeChangeLogRepository;
  private final SortHandlerMethodArgumentResolverCustomizer sortHandlerMethodArgumentResolverCustomizer;

  @GetMapping
  public ResponseEntity<CursorPageResponseEmployeeDto> getChangeLogs(
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) ModifyType type,
      @RequestParam(required = false) String memo,
      @RequestParam(required = false) String ipAddress,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Instant atFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Instant atTo,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "at") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection,
      Sort sort) {

    ChangeLogRequest request = new ChangeLogRequest(idAfter, size, employeeNumber, type, memo,
        ipAddress, atFrom, atTo, sortField, sortDirection);

    CursorPageResponseEmployeeDto response =  employeeChangeLogRepository.findChangeLogs(request);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}/diffs")
  public ResponseEntity<List<DiffDto>> getChangeLogDetail(
      @PathVariable(value = "id") Long id
  ) {
    return null;
  }

  @GetMapping("/count")
  public Long getChangeLogCount() {
    return null;
  }
}
