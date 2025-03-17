package com.codeit.sb01hrbankteam04.domain.department.service;

import com.codeit.sb01hrbankteam04.domain.department.dto.DepartmentCreateRequest;
import com.codeit.sb01hrbankteam04.domain.department.dto.DepartmentDto;
import com.codeit.sb01hrbankteam04.domain.department.entity.Department;
import com.codeit.sb01hrbankteam04.domain.department.repository.DepartmentRepository;
import com.codeit.sb01hrbankteam04.global.exception.CustomException;
import com.codeit.sb01hrbankteam04.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

}
