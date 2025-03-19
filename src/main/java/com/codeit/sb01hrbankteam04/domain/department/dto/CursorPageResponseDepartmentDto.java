package com.codeit.sb01hrbankteam04.domain.department.dto;

import java.util.List;

public record CursorPageResponseDepartmentDto(
        List<DepartmentDto> content,
        String nextCursor,
        Long nextIdAfter,
        int size,
        Long totalElements,
        boolean hasNext
) {
    public static CursorPageResponseDepartmentDto from(
            List<DepartmentDto> departments,
            String nextCursor,
            Long nextIdAfter,
            int size,
            Long totalElements,
            boolean hasNext
    ) {
        return new CursorPageResponseDepartmentDto(departments, nextCursor, nextIdAfter, size, totalElements, hasNext);
    }
}
