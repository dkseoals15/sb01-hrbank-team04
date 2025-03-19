package com.codeit.sb01hrbankteam04.domain.file.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * FileController에 사용할 Swagger 문서 정의
 */
@Tag(name = "파일 관리", description = "파일 관리 API")
public interface FileControllerDocs {

  /**
   * 파일 다운로드 API.
   *
   * @param id 다운로드할 파일의 ID
   * @return 파일 다운로드 응답
   */
  @Operation(
      summary = "파일 다운로드",
      description = "Path를 통해 ID를 받아 파일을 다운로드합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "파일 다운로드 성공",
              content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)),
          @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음")
      }
  )
  @GetMapping("/{id}/download")
  ResponseEntity<?> downloadFile(@PathVariable Long id);

}
