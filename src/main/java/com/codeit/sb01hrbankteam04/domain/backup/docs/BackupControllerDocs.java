package com.codeit.sb01hrbankteam04.domain.backup.docs;

import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupRequestDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.CursorPageResponseBackupDto;
import com.codeit.sb01hrbankteam04.global.response.CustomApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * BackupController의 Swagger를 위한 Docs 파일
 */
@Tag(name = "데이터 백업 관리", description = "데이터 백업 관리 API")
public interface BackupControllerDocs {

  /**
   * 백업 생성 스웨거
   */
  @Operation(
      summary = "백업 생성",
      description = "새로운 백업 파일 생성"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "백업 생성 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = BackupDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  CustomApiResponse<?> create(HttpServletRequest request);

  /**
   * 백업 목록 조회 스웨거
   */
  @Operation(
      summary = "백업 목록 조회",
      description = "필터링 된 백업 목록을 커서 기반 페이지네이션 방식으로 조회"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "백업 목록 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = CursorPageResponseBackupDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  CustomApiResponse<CursorPageResponseBackupDto<BackupDto>> getBackupList(
      @Parameter(description = "백업 목록 요청 DTO", required = true)
      @ModelAttribute BackupRequestDto backupRequestDto);

  /**
   * 최신 백업 조회 스웨거
   */
  @Operation(
      summary = "최신 백업 조회",
      description = "최신 백업 정보를 조회"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "최신 백업 조회 성공",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = BackupDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "500", description = "서버 오류")
  })
  CustomApiResponse<BackupDto> getLatestBackup(
      @Parameter(description = "백업 상태 (COMPLETED | FAILED | IN_PROGRESS), 기본값 (COMPLETED)")
      @RequestParam(defaultValue = "COMPLETED") String status);
}
