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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.management.InstanceAlreadyExistsException;
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
        .joinedAt(dateToInstant(employeeCreateRequest.hireDate()))
        .profile(nullableProfile)
        .build();

    employeeRepository.save(employee);
    return employeeMapper.toDto(employee);
  }

  //"yyyy-MM-dd" String을 Instant로 변환하는 코드
  private Instant dateToInstant(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //문자열을 localDate 변환
    LocalDate localDate = LocalDate.parse(date, formatter);

    //LocalDate를 Instant 형식으로 변환, (00:00 UT
    return localDate.atStartOfDay(ZoneId.of("UTC")).toInstant();
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

  @Transactional
  @Override
  public EmployeeResponse update(Long id, EmployeeUpdateRequest employeeUpdateRequest,
      Optional<FileDto> optionalProfileRequest) throws InstanceAlreadyExistsException {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(()-> new NoSuchElementException("no such employee with id " + id));

    String newEmail= employeeUpdateRequest.email();
    //이메일 검증
    if(employeeRepository.existsByEmail(newEmail)) {
      throw new InstanceAlreadyExistsException("email exists: " + newEmail);
    }

    //근무 상태의 enum 일치 방어 처리
    if(!Arrays.stream(EmployeeStatusType.values())
        .anyMatch(status -> status.name().equals(employeeUpdateRequest.status()))){
      throw new NoSuchElementException("no such employee status: " + employeeUpdateRequest.status());
    }
    EmployeeStatusType newStatus = EmployeeStatusType.valueOf(employeeUpdateRequest.status());


    //부서 찾기
    Department newDepartment = departmentRepository.findById(employeeUpdateRequest.departmentId())
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

    String newName= employeeUpdateRequest.name();
    String newPosition= employeeUpdateRequest.position();
    Instant newHireDate = dateToInstant(employeeUpdateRequest.hireDate());

    employee.update(newName,newEmail,newDepartment,newPosition,newHireDate,newStatus, nullableProfile);

    employeeRepository.save(employee);
    return employeeMapper.toDto(employee);
  }

  @Override
  public void delete(Long id) {
    if(!employeeRepository.existsById(id)) {
      throw new NoSuchElementException("no such employee with id " + id);
    }
    employeeRepository.deleteById(id);
  }
}