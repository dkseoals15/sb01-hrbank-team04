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
                .establishedDate(request.establishedDate())
                .build();

        // 저장 및 응답 봔환
        return DepartmentDto.fromEntity(departmentRepository.save(department));
    }

    @Transactional(readOnly = true)
    public CursorPageResponseDepartmentDto getDepartments(
            String name, String description, Long nextIdAfter, String sortBy, int size) {

        // 페이지 정보 생성
        Pageable pageable = PageRequest.of(0, size);

        // 부서 목록 조회 (페이징 적용)
        List<Department> departments = departmentRepository.findDepartmentsByCursor(
                name, description, nextIdAfter, sortBy, pageable);

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

        // 중복 이름 체크
        departmentRepository.findByName(request.name()).ifPresent(existing -> {
            if(!existing.getId().equals(id)) {
                throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
            }
        });

        // 데이터 업데이트
        department.updateDepartment(request.name(), request.description(), request.establishedDate());

        // 저장후 변환
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
