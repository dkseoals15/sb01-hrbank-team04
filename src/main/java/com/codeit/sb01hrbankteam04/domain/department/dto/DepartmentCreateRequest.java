package com.codeit.sb01hrbankteam04.domain.department.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record DepartmentCreateRequest(
        String name,

        String description,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        String establishedDate
) {
        @JsonIgnore
        public Instant getEstablishedDateAsInstant() {
                return LocalDate.parse(establishedDate).atStartOfDay().toInstant(ZoneOffset.UTC);
        }
}
