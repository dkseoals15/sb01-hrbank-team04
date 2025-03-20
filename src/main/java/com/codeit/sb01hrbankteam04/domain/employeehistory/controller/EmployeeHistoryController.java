package com.codeit.sb01hrbankteam04.domain.employeehistory.controller;

import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request.ChangeLogRequest;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request.CountFilterRequest;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.CursorPageResponseEmployeeDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.DiffDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.repository.EmployeeChangeLogRepository;
import com.codeit.sb01hrbankteam04.domain.employeehistory.type.ModifyType;
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

  private final EmployeeChangeLogRepository repository;

  /**
   * @param employeeNumber 대상 직원 사번
   * @param type           이력 유형 (CREATED,UPDATED,DELETED)
   * @param memo           내용
   * @param ipAddress      IP 주소
   * @param atFrom         수정 일시 (부터)
   * @param atTo           수정 일시 (까지)
   * @param idAfter        이전 페이지 마지막 요소 ID
   * @param cursor         이전 페이지의 마지막 ID
   * @param size           페이지 크기
   * @param sortField      정렬 필드 (ipAddress, at)
   * @param sortDirection  정렬 방향 (asc, desc)
   * @return 직원 정보 수정 이력 목록을 조회, 상세 변경은 포함하지 않음
   */
  @GetMapping
  public ResponseEntity<CursorPageResponseEmployeeDto> getChangeLogs(
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) ModifyType type,
      @RequestParam(required = false) String memo,
      @RequestParam(required = false) String ipAddress,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Instant atFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Instant atTo,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "at") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection) {

    ChangeLogRequest request = new ChangeLogRequest(idAfter, size, employeeNumber, type, memo,
        cursor,
        ipAddress, atFrom, atTo, sortField, sortDirection);

    CursorPageResponseEmployeeDto response = repository.findChangeLogs(request);

    return ResponseEntity.ok(response);
  }

  /**
   * @param id 이력 ID -> 직원 엔티티 ID 아님
   * @return 직원 정보수정 이력의 상세 정보를 조회 (before 값과 after 값을 확인할 수 있음)
   */

  @GetMapping("/{id}/diffs")
  public ResponseEntity<List<DiffDto>> getRevisionDetails(@PathVariable("id") Long id) {
    List<DiffDto> diffs = repository.getRevisionDetails(id);
    return ResponseEntity.ok(diffs);
  }

  /**
   * @param fromDate 시작일시 값이 없다면 7일전 값
   * @param toDate   종료 일시 값이 없다면 현재
   * @return 직원 정보 수정 이력 건수를 조회함
   */

  @GetMapping("/count")
  public ResponseEntity<Long> getChangeLogCount(
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Instant fromDate,
      @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE_TIME) Instant toDate
  ) {

    Instant now = Instant.now();

    if (fromDate == null) {
      fromDate = now.minus(7, ChronoUnit.DAYS);
    }

    if (toDate == null) {
      toDate = now;
    }

    // `toDate`가 `fromDate`보다 과거이면 0 반환
    if (toDate.isBefore(fromDate)) {
      return ResponseEntity.ok(0L);
    }

    CountFilterRequest request = new CountFilterRequest(fromDate, toDate);

    Long count = repository.countChangeLogs(request);

    return ResponseEntity.ok(count);
  }
}
