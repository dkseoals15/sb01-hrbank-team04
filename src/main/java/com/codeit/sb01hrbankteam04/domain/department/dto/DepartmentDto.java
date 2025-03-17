package com.codeit.sb01hrbankteam04.domain.department.dto;

import com.codeit.sb01hrbankteam04.domain.department.entity.Department;

import java.time.LocalDateTime;

public record DepartmentDto(
        Long id,
        String name,
        String description,
        LocalDateTime establishedDate,
        int employeeCount
) {
    public static DepartmentDto fromEntity(Department department) {
        return new DepartmentDto(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getEstablishedAt(),
                0 // TODO: 직원 수 계산 로직 추가 예정
        );
    }
}
