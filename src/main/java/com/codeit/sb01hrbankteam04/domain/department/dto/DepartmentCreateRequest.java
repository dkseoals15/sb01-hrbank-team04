package com.codeit.sb01hrbankteam04.domain.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DepartmentCreateRequest(
        @NotBlank(message = "부서 이름은 필수 입력값입니다.") String name,
        @NotBlank(message = "부서 설명은 필수 입력값입니다.") String description,
        @NotNull(message = "설립일은 필수 입력값입니다.")LocalDateTime establishedDate
        ) {
}
