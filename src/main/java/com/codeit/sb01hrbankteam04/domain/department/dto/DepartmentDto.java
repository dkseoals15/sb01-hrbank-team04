package com.codeit.sb01hrbankteam04.domain.department.dto;

import com.codeit.sb01hrbankteam04.domain.department.entity.Department;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DepartmentDto(
        Long id,
        String name,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate establishedDate,
        int employeeCount // TODO: 직원 수 계산 로직 추가 예정
) {
    public static DepartmentDto fromEntity(Department department) {
        return new DepartmentDto(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getEstablishedDate(),
                0 // 직원 수 계산 로직 추가 예정
        );
    }
}
