package com.codeit.sb01hrbankteam04.domain.employee.mapper;

import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeResponse;
import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

  public EmployeeResponse toDto(Employee employee) {

    return new EmployeeResponse(
        employee.getId(),
        employee.getName(),
        employee.getEmail(),
        employee.getCode(),
        employee.getDepartment().getId(),
        employee.getDepartment().getName(),
        employee.getPosition(),
        employee.getJoinedAt(),
        employee.getStatus().toString(),
        employee.getProfile() != null ? employee.getProfile().getId() : null
// profile이 null이면 null 반환
    );
  }


}
