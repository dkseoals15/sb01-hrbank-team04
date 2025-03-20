package com.codeit.sb01hrbankteam04.domain.department.controller;

import com.codeit.sb01hrbankteam04.domain.department.dto.CursorPageResponseDepartmentDto;
import com.codeit.sb01hrbankteam04.domain.department.dto.DepartmentCreateRequest;
import com.codeit.sb01hrbankteam04.domain.department.dto.DepartmentDto;
import com.codeit.sb01hrbankteam04.domain.department.dto.DepartmentUpdateRequest;
import com.codeit.sb01hrbankteam04.domain.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 부서(Department) 관련 API를 제공하는 컨트롤러
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

  private final DepartmentService departmentService;

  /**
   * 새로운 부서를 생성하는 API - 요청 본문에 `DepartmentCreateRequest`를 받음 - 생성된 부서 정보를 `DepartmentDto`로 반환
   *
   * @param request 부서 생성 요청 DTO
   * @return 생성된 부서 정보 (`DepartmentDto`)
   */
  @PostMapping
  public ResponseEntity<DepartmentDto> createDepartment(
      @RequestBody DepartmentCreateRequest request) {
    return ResponseEntity.ok(departmentService.createDepartment(request));
  }

  /**
   * 특정 부서 정보를 조회하는 API - 부서 ID를 기반으로 조회 - 존재하지 않는 경우 예외 발생
   *
   * @param id 조회할 부서 ID
   * @return 조회된 부서 정보 (`DepartmentDto`)
   */
  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDto> getDepartment(@PathVariable Long id) {
    return ResponseEntity.ok(departmentService.getDepartmentById(id));
  }

  /**
   * 부서 목록을 조회하는 API (검색, 정렬, 페이징 지원) - 특정 키워드(nameOrDescription)로 검색 가능 - 정렬 기준(`sortField`), 정렬
   * 방향(`sortDirection`) 지정 가능 - 커서 기반 페이지네이션 지원 (`idAfter`, `cursor`)
   *
   * @param nameOrDescription 검색할 부서명 또는 설명 (선택)
   * @param idAfter           특정 ID 이후 데이터 조회 (선택)
   * @param cursor            커서 기반 페이지네이션 (선택)
   * @param size              조회할 데이터 개수 (기본값 10)
   * @param sortField         정렬 기준 (`name`, `establishedDate`) (기본값 `establishedDate`)
   * @param sortDirection     정렬 방향 (`asc`, `desc`) (기본값 `asc`)
   * @return 검색된 부서 목록 및 페이지네이션 정보 (`CursorPageResponseDepartmentDto`)
   */
  @GetMapping
  public CursorPageResponseDepartmentDto getDepartments(
      @RequestParam(required = false) String nameOrDescription,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "establishedDate") String sortField,
      @RequestParam(defaultValue = "asc") String sortDirection) {

    return departmentService.getDepartments(nameOrDescription, idAfter, cursor, size, sortField,
        sortDirection);
  }

  /**
   * 부서 정보를 업데이트하는 API - 부서 ID를 기반으로 기존 정보를 수정 - 요청된 값이 `null`이면 기존 값을 유지
   *
   * @param id      업데이트할 부서 ID
   * @param request 업데이트 요청 DTO
   * @return 업데이트된 부서 정보 (`DepartmentDto`)
   */
  @PatchMapping("/{id}")
  public ResponseEntity<DepartmentDto> updateDepartment(
      @PathVariable Long id,
      @RequestBody DepartmentUpdateRequest request) {

    DepartmentDto updatedDepartment = departmentService.updateDepartment(id, request);
    return ResponseEntity.ok(updatedDepartment);
  }

  /**
   * 부서를 삭제하는 API - 부서 ID를 기반으로 삭제 수행
   *
   * @param id 삭제할 부서 ID
   * @return 응답 본문 없이 `204 No Content` 반환
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
    departmentService.deleteDepartment(id);
    return ResponseEntity.noContent().build();
  }
}
