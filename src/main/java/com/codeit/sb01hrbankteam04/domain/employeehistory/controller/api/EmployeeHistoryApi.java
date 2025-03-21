package com.codeit.sb01hrbankteam04.domain.employeehistory.controller.api;

import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.CursorPageResponseEmployeeDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.DiffDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.type.ModifyType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@Tag(name = "직원 정보 수정 이력 관리", description = "직원 정보 수정 이력 API")
@RequestMapping("/api/change-logs")
public interface EmployeeHistoryApi {

  @Operation(summary = "직원 정보 수정 이력 목록 조회", description = "직원 정보 수정 이력을 조회합니다. 상세 변경 사항은 포함되지 않습니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseEmployeeDto.class)))
  })
  @GetMapping
  ResponseEntity<CursorPageResponseEmployeeDto> getChangeLogs(
      @Parameter(description = "직원 사번") @RequestParam(required = false) String employeeNumber,
      @Parameter(description = "이력 유형 (CREATED, UPDATED, DELETED)") @RequestParam(required = false) ModifyType type,
      @Parameter(description = "메모") @RequestParam(required = false) String memo,
      @Parameter(description = "IP 주소") @RequestParam(required = false) String ipAddress,
      @Parameter(description = "수정 일시 (부터)")
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant atFrom,
      @Parameter(description = "수정 일시 (까지)")
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant atTo,
      @Parameter(description = "이전 페이지 마지막 요소 ID") @RequestParam(required = false) Long idAfter,
      @Parameter(description = "커서 ID") @RequestParam(required = false) String cursor,
      @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "정렬 필드(at || ipAddress)", example = "at") @RequestParam(defaultValue = "at") String sortField,
      @Parameter(description = "정렬 방향(asc || desc)", example = "desc") @RequestParam(defaultValue = "desc") String sortDirection
  );

  @Operation(summary = "직원 정보 수정 이력 상세 변경 조회", description = "특정 이력 ID에 대한 before/after 변경 내역을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = DiffDto.class)))),
      @ApiResponse(responseCode = "404", description = "해당 이력 ID가 존재하지 않음")
  })
  @GetMapping("/{id}/diffs")
  ResponseEntity<List<DiffDto>> getRevisionDetails(
      @Parameter(description = "이력 ID", required = true) @PathVariable("id") Long id
  );

  @Operation(summary = "직원 이력 카운트 조회", description = "특정 기간 내 직원 정보 수정 이력 건수를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Long.class)))
  })
  @GetMapping("/count")
  ResponseEntity<Long> getChangeLogCount(
      @Parameter(description = "시작 일시 (기본 : 7일 전)")
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
      @Parameter(description = "종료 일시 (기본 : now())")
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate
  );
}

