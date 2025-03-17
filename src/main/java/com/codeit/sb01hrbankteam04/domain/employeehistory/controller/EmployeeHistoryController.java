package com.codeit.sb01hrbankteam04.domain.employeehistory.controller;

import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.CursorPageResponseEmployeeDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.DiffDto;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/change-logs")
public class EmployeeHistoryController {

  @GetMapping
  public ResponseEntity<CursorPageResponseEmployeeDto> getChangeLogs() {
    return null;
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
