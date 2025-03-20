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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    @Transactional
    public DepartmentDto createDepartment(DepartmentCreateRequest request)
    {
        //이름 중복 검사
        if (departmentRepository.findByName(request.name()).isPresent()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        //부서 생성
        Department department = Department.builder()
                .name(request.name())
                .description(request.description())
                .establishedDate(request.getEstablishedDateAsInstant())
                .build();

        // 저장 및 응답 봔환
        return DepartmentDto.fromEntity(departmentRepository.save(department));
    }

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

    // Cursor 값을 Base64 인코딩하여 변환
    private String encodeCursor(Long id) {
        return Base64.getEncoder().encodeToString(("{\"id\":" + id + "}").getBytes(StandardCharsets.UTF_8));
    }

    @Transactional(readOnly = true)
    public DepartmentDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));
        return DepartmentDto.fromEntity(department);
    }

    @Transactional
    public DepartmentDto updateDepartment(Long id, DepartmentUpdateRequest request) {
        // 존재 여부 확인
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        // 기존 값 유지하면서 수정된 값만 반영
        String newName = request.name() != null ? request.name() : department.getName();
        String newDescription = request.description() != null ? request.description() : department.getDescription();
        Instant
                newEstablishedDate = request.establishedDate() != null ? request.getEstablishedDateAsInstant() : department.getEstablishedDate();

        // 중복 이름 체크 (name이 null이 아닐 때만 실행!)
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


    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

//        if (employeeRepository.existsByDepartmentId(id)) {
//            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
//        }

        departmentRepository.delete(department);
    }


}
