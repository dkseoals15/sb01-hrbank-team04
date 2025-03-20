package com.codeit.sb01hrbankteam04.domain.employee.dto;


public record EmployeeCreateRequest(
    String name,
    String email,
    Long departmentId,
    String position,
    String hireDate,
    String memo
) {

}
