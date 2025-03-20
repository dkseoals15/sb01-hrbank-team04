package com.codeit.sb01hrbankteam04.domain.employee.dto;

public record EmployeeUpdateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    String hireDate, // TODO: Update 테스트용 수정
    String status,
    String memo
) {

}
