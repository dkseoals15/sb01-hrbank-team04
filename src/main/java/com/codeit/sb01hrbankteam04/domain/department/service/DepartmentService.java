package com.codeit.sb01hrbankteam04.domain.department.service;

import com.codeit.sb01hrbankteam04.domain.department.dto.CursorPageResponseDepartmentDto;
import com.codeit.sb01hrbankteam04.domain.department.dto.DepartmentCreateRequest;
import com.codeit.sb01hrbankteam04.domain.department.dto.DepartmentDto;
import com.codeit.sb01hrbankteam04.domain.department.dto.DepartmentUpdateRequest;
import com.codeit.sb01hrbankteam04.domain.department.entity.Department;
import com.codeit.sb01hrbankteam04.domain.department.repository.DepartmentRepository;
import com.codeit.sb01hrbankteam04.global.exception.CustomException;
import com.codeit.sb01hrbankteam04.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 부서(Department) 관련 비즈니스 로직을 담당하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    /**
     * 새로운 부서를 생성하는 메서드
     * - 부서명이 중복되지 않는지 검사
     * - 부서 정보 저장 후 DTO 변환하여 반환
     * @param request 부서 생성 요청 DTO
     * @return 생성된 부서의 DTO
     */
    @Transactional
    public DepartmentDto createDepartment(DepartmentCreateRequest request) {
        // 이름 중복 검사
        if (departmentRepository.findByName(request.name()).isPresent()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 부서 생성
        Department department = Department.builder()
                .name(request.name())
                .description(request.description())
                .establishedDate(request.getEstablishedDateAsInstant())
                .build();

        // 저장 및 응답 반환
        return DepartmentDto.fromEntity(departmentRepository.save(department));
    }

    /**
     * 부서 목록을 조회하는 메서드 (검색, 정렬, 페이지네이션 지원)
     * - 특정 키워드(nameOrDescription) 포함된 부서 검색 가능
     * - ID 기반 커서 페이지네이션 지원 (nextIdAfter)
     * - 정렬 기준 및 방향 적용 (sortField, sortDirection)
     * @param nameOrDescription 검색할 부서명 또는 설명
     * @param idAfter 특정 ID 이후 데이터 조회
     * @param cursor 커서 기반 페이지네이션 (Base64 인코딩된 값)
     * @param size 조회할 데이터 개수
     * @param sortField 정렬 기준 (`name`, `establishedDate`)
     * @param sortDirection 정렬 방향 (`asc`, `desc`)
     * @return 부서 목록과 페이지네이션 정보 포함 DTO
     */
    @Transactional(readOnly = true)
    public CursorPageResponseDepartmentDto getDepartments(
            String nameOrDescription, Long idAfter, String cursor, int size, String sortField, String sortDirection) {

        // 부서 목록 조회 (페이징 적용)
        List<Department> departments = departmentRepository.findDepartmentsByCursor(
                nameOrDescription, idAfter, sortField, sortDirection, size);

        // `DepartmentDto` 변환
        List<DepartmentDto> departmentDtos = departments.stream()
                .map(DepartmentDto::fromEntity)
                .collect(Collectors.toList());

        // 다음 페이지 정보 계산
        Long lastId = departments.isEmpty() ? null : departments.get(departments.size() - 1).getId();
        String nextCursor = lastId != null ? encodeCursor(lastId) : null;
        boolean hasNext = departments.size() == size;

        // 총 부서 개수 조회
        Long totalElements = departmentRepository.count();

        // 결과 반환
        return CursorPageResponseDepartmentDto.from(
                departmentDtos, nextCursor, lastId, size, totalElements, hasNext);
    }

    /**
     * 커서 값을 Base64로 인코딩하여 변환하는 메서드
     * - 페이징을 위해 `id`를 인코딩하여 커서로 사용
     * @param id 커서로 사용할 ID 값
     * @return Base64 인코딩된 커서 값
     */
    private String encodeCursor(Long id) {
        return Base64.getEncoder().encodeToString(("{\"id\":" + id + "}").getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 특정 부서 정보를 조회하는 메서드
     * - ID 기준으로 조회하며, 존재하지 않으면 예외 발생
     * @param id 조회할 부서의 ID
     * @return 조회된 부서의 DTO
     */
    @Transactional(readOnly = true)
    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));
        return DepartmentDto.fromEntity(department);
    }

    /**
     * 부서 정보를 업데이트하는 메서드
     * - 존재하는 부서를 찾아서 변경된 정보만 반영
     * - 이름이 변경될 경우 중복 검사 수행
     * @param id 업데이트할 부서 ID
     * @param request 업데이트 요청 DTO
     * @return 업데이트된 부서의 DTO
     */
    @Transactional
    public DepartmentDto updateDepartment(Long id, DepartmentUpdateRequest request) {
        // 존재 여부 확인
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        // 기존 값 유지하면서 수정된 값만 반영
        String newName = request.name() != null ? request.name() : department.getName();
        String newDescription = request.description() != null ? request.description() : department.getDescription();
        Instant newEstablishedDate = request.establishedDate() != null ? request.getEstablishedDateAsInstant() : department.getEstablishedDate();

        // 중복 이름 체크 (name이 null이 아닐 때만 실행)
        if (request.name() != null) {
            departmentRepository.findByName(request.name()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
                }
            });
        }

        // 데이터 업데이트
        department.updateDepartment(newName, newDescription, newEstablishedDate);

        // 저장 후 변환
        return DepartmentDto.fromEntity(department);
    }

    /**
     * 부서를 삭제하는 메서드
     * - 존재하는 부서를 찾아 삭제
     * - (추후 직원 테이블 연관 관계 검증 가능)
     * @param id 삭제할 부서 ID
     */
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        // 🔹 만약 직원과 연관된 경우 삭제 방지 로직 추가 가능
        // if (employeeRepository.existsByDepartmentId(id)) {
        //     throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        // }

        departmentRepository.delete(department);
    }
}
