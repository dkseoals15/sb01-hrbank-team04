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
import jakarta.transaction.Transactional;
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



  //파일 저장 로직 + 직원 등록 로직 트랜잭션 관리
  @Transactional
  @Override
  public EmployeeResponse create(EmployeeCreateRequest employeeCreateRequest,
      Optional<FileDto> optionalProfileRequest) {

    //부서 찾기
    Department department = departmentRepository.findById(employeeCreateRequest.departmentId())
        .orElseThrow(() -> new NoSuchElementException("Department not found"));

    //파일 저장
    File nullableProfile = optionalProfileRequest
        .map(profileRequst -> {
          String filename = profileRequst.Filename();
          String contentType = profileRequst.ContentType();
          byte[] bytes = profileRequst.Bytes();
          File file = new File(filename, contentType, (long) bytes.length);
          fileRepository.save(file);
          return file;
        })
        .orElse(null);

    Employee employee = Employee.builder()
        .status(EmployeeStatusType.재직중)
        .name(employeeCreateRequest.name())
        .email(employeeCreateRequest.email())
        .code("2025-TEMPCODE") //TODO: 코드 생성 로직 추가해야함
        .department(department)
        .position(employeeCreateRequest.position())
        .joinedAt(Instant.now())
        .profile(nullableProfile)
        .build();

    employeeRepository.save(employee);
    return employeeMapper.toDto(employee);
  }


  @Override
  public EmployeeResponse find(Long id) {
    return employeeRepository.findById(id)
            .map(employeeMapper::toDto)
            .orElseThrow(()-> new NoSuchElementException("no such employee with id "  + id));
  }

  @Override
  public List<EmployeeResponse> findAll() {
    return employeeRepository.findAll()
        .stream()
        .map(employeeMapper::toDto)
        .toList();
  }

  @Override
  public EmployeeResponse update(Long id, EmployeeUpdateRequest employeeUpdateRequest,
      Optional<FileDto> proile) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(()-> new NoSuchElementException("no such employee with id " + id));



    return null;
  }

  @Override
  public void delete(Long id) {
    if(!employeeRepository.existsById(id)) {
      throw new NoSuchElementException("no such employee with id " + id);
    }
    employeeRepository.deleteById(id);
  }
}