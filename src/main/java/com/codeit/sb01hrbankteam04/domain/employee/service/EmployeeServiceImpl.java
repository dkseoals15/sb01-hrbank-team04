package com.codeit.sb01hrbankteam04.domain.employee.service;

import com.codeit.sb01hrbankteam04.domain.department.Department;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeUpdateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.domain.employee.repository.DepartmentRepository;
import com.codeit.sb01hrbankteam04.domain.employee.repository.EmployeeRepository;
import com.codeit.sb01hrbankteam04.domain.employee.repository.FileRepository;
import com.codeit.sb01hrbankteam04.domain.file.File;
import com.codeit.sb01hrbankteam04.domain.file.FileDto;
import com.codeit.sb01hrbankteam04.domain.mapper.EmployeeMapper;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final FileRepository fileRepository;
  private final EmployeeMapper employeeMapper;



  @Override
  public EmployeeResponse create(EmployeeCreateRequest employeeCreateRequest,
      Optional<FileDto> profileRequest) {

    //temp department
    Department department = departmentRepository.findById(employeeCreateRequest.departmentId())
        .orElseThrow(() -> new NoSuchElementException("Department not found"));

    File profile = fileRepository.findIdByName(profileRequest.)

    Employee employee = Employee.builder()
        .status(EmployeeStatusType.재직중)
        .name(employeeCreateRequest.name())
        .email(employeeCreateRequest.email())
        .code("2025-TEMPCODE")
        .department(department)
        .position(employeeCreateRequest.position())
        .joinedAt(Instant.now())
        .profile(profile)
        .build();
    System.out.println(employeeMapper.toDto(employee));
    employeeRepository.save(employee);
    return null;
  }


  @Override
  public EmployeeResponse find(Long id) {
    return employeeRepository.findById(id)
            .map(employeeMapper::toDto)
            .orElseThrow(()-> new NoSuchElementException("no such employee with id "  + id));
  }
  @Override
  public List<EmployeeResponse> findAll() {
    return List.of();
  }

  @Override
  public EmployeeResponse update(Long id, EmployeeUpdateRequest employeeUpdateRequest,
      Optional<FileDto> proile) {
    return null;
  }

  @Override
  public void delete(Long id) {

  }
}