package com.codeit.sb01hrbankteam04.domain.employee.dto;

public record EmployeeUpdateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    String hireDate,
    String status,
    String memo
)
{

}
