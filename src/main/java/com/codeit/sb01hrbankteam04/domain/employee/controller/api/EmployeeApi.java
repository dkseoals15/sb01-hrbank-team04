package com.codeit.sb01hrbankteam04.domain.employee.controller.api;

import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

//Swagger 문서 컨트롤
@Tag(name="직원 관리", description = "직원 관리 API")
public interface EmployeeApi {
  
  //직원 목록 조회
  
  //직원 등록
  @Operation(summary = "직원 등록", description = "새로운 직원을 등록합니다.")
  @ApiResponses(value= {
      @ApiResponse(
          responseCode = "200", description = "등록 성공",
          content = @Content(schema = @Schema(implementation = EmployeeDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Department with id {departmentId } not found"))
      ),
  })
  ResponseEntity<EmployeeDto> createEmployee(
      @Parameter(
          description = "직원 등록 요청",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
      )EmployeeCreateRequest employeeCreateRequest,
      @Parameter(
          description = "프로필 이미지",
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
      )MultipartFile profile
  );

  //직원 상세 조회
  
  //직원 삭제
  
  //직원 수정

}
