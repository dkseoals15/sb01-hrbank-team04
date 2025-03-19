package com.codeit.sb01hrbankteam04.domain.employee.service;

import com.codeit.sb01hrbankteam04.domain.department.Department;
import com.codeit.sb01hrbankteam04.domain.department.DepartmentRepository;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeePageResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeResponse;
import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.domain.employee.repository.EmployeeRepository;
import com.codeit.sb01hrbankteam04.domain.file.repository.FileRepository;
import com.codeit.sb01hrbankteam04.domain.mapper.EmployeeMapper;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeDistributionResponse;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeTrendResponse;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.management.InstanceAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final FileRepository fileRepository;
  private final EmployeeMapper employeeMapper;

  /// 직원 관리 CRUD
  /// 


//  //파일 저장 로직 + 직원 등록 로직 트랜잭션 관리
//  @Transactional
//  public EmployeeResponse create(EmployeeCreateRequest employeeCreateRequest,
//      Optional<FileDto> optionalProfileRequest) {
//
//    //부서 찾기
//    Department department = departmentRepository.findById(employeeCreateRequest.departmentId())
//        .orElseThrow(() -> new NoSuchElementException("Department not found"));
//
//    //파일 저장
//    File nullableProfile = optionalProfileRequest
//        .map(profileRequst -> {
//          String filename = profileRequst.Filename();
//          String contentType = profileRequst.ContentType();
//          byte[] bytes = profileRequst.Bytes();
//          File file = new File(filename, contentType, (long) bytes.length);
//          fileRepository.save(file);
//          return file;
//        })
//        .orElse(null);
//
//    Employee employee = Employee.builder()
//        .status(EmployeeStatusType.ACTIVE)
//        .name(employeeCreateRequest.name())
//        .email(employeeCreateRequest.email())
//        .department(department)
//        .position(employeeCreateRequest.position())
//        .joinedAt(dateToInstant(employeeCreateRequest.hireDate()))
//        //.profile(nullableProfile) //TODO: 오류수정, 우선 주석처리
//        .build();
//
//    employeeRepository.save(employee);
//    return employeeMapper.toDto(employee);
//  }

  //"yyyy-MM-dd" String을 Instant로 변환하는 코드
  private Instant dateToInstant(String date) {
    if (date==null){
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //문자열을 localDate 변환
    LocalDate localDate = LocalDate.parse(date,  DateTimeFormatter.ISO_DATE);
    //LocalDate를 Instant 형식으로 변환, (00:00 UT
    Instant instant = localDate.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant();
    return instant;
  }


  public EmployeePageResponse getEmployees(
      String nameOrEmail, String employeeNumber, String departmentName, String position,
      String hireDateFrom, String hireDateTo, String status,
      Long nextIdAfter, String cursor, String sortBy, int size) {

    // Pageable 객체를 생성
    Pageable pageable = PageRequest.of(0, size);

    //TODO: 커서 적용

    // nextIdAfter를 구할 때, 커서 값이 있을 경우, 이를 통해 nextIdAfter 값을 결정
    Long nextId = (cursor != null) ? decodeCursor(cursor) : nextIdAfter;


    List<Employee> employees = employeeRepository.findEmployeesByCursor(
        nameOrEmail, employeeNumber, departmentName, position,
        hireDateFrom, hireDateTo, status, nextIdAfter, sortBy, pageable);

    // pageResponse  dto로 변환
    List<EmployeeResponse> EmployeeResponses = employees.stream()
        .map(employeeMapper::toDto)
        .collect(Collectors.toList());

    // next page
    Long lastId = employees.isEmpty() ? null : employees.get(employees.size() - 1).getId();//nextIdAfter
    String nextCursor = lastId != null ? encodeCursor(lastId) : null;
    boolean hasNext = employees.size() == size;

    Long totalElements = employeeRepository.count();

    return EmployeePageResponse.from(
        EmployeeResponses, nextCursor, lastId, size, totalElements, hasNext);
  }

  // 커서 인코딩
  private String encodeCursor(Long id) {
    return Base64.getEncoder().encodeToString(("{\"id\":" + id + "}").getBytes(StandardCharsets.UTF_8));
  }

  // 커서 값 디코딩
  public static Long decodeCursor(String cursor) {
    if (cursor == null || cursor.isEmpty()) {
      return null;
    }

    // Base64로 디코딩 후 Long 값으로 변환
    String decodedString = new String(Base64.getDecoder().decode(cursor));
    return Long.valueOf(decodedString);
  }


  public EmployeeResponse find(Long id) {
    return employeeRepository.findById(id)
        .map(employeeMapper::toDto)
        .orElseThrow(()-> new NoSuchElementException("no such employee with id "  + id));
  }




  private List<EmployeeResponse> findAll() {
    return employeeRepository.findAll()
        .stream()
        .map(employeeMapper::toDto)
        .toList();
  }

//  @Transactional
//  public EmployeeResponse update(Long id, EmployeeUpdateRequest employeeUpdateRequest,
//      Optional<FileDto> optionalProfileRequest) throws InstanceAlreadyExistsException {
//    Employee employee = employeeRepository.findById(id)
//        .orElseThrow(()-> new NoSuchElementException("no such employee with id " + id));
//
//    String newEmail= employeeUpdateRequest.email();
//    //이메일 검증
//    if(employeeRepository.existsByEmail(newEmail)) {
//      throw new InstanceAlreadyExistsException("email exists: " + newEmail);
//    }
//
//    //근무 상태의 enum 일치 방어 처리
//    if(!Arrays.stream(EmployeeStatusType.values())
//        .anyMatch(status -> status.name().equals(employeeUpdateRequest.status()))){
//      throw new NoSuchElementException("no such employee status: " + employeeUpdateRequest.status());
//    }
//    EmployeeStatusType newStatus = EmployeeStatusType.valueOf(employeeUpdateRequest.status());
//
//
//    //부서 찾기
//    Department newDepartment = departmentRepository.findById(employeeUpdateRequest.departmentId())
//        .orElseThrow(() -> new NoSuchElementException("Department not found"));
//
//    //파일 저장
//    File nullableProfile = optionalProfileRequest
//        .map(profileRequst -> {
//          String filename = profileRequst.Filename();
//          String contentType = profileRequst.ContentType();
//          byte[] bytes = profileRequst.Bytes();
//          File file = new File(filename, contentType, (long) bytes.length);
//          fileRepository.save(file);
//          return file;
//        })
//        .orElse(null);
//
//    String newName= employeeUpdateRequest.name();
//    String newPosition= employeeUpdateRequest.position();
//    Instant newHireDate = dateToInstant(employeeUpdateRequest.hireDate());
//
//    //TODO: file 타입에 맞추기
//    //employee.update(newName,newEmail,newDepartment,newPosition,newHireDate,newStatus, nullableProfile);
//
//    employeeRepository.save(employee);
//    return employeeMapper.toDto(employee);
//  }
//
//  @Transactional
//  public void delete(Long id) {
//    Employee employee = employeeRepository.findById(id)
//        .orElseThrow(() -> new NoSuchElementException("no such employee with id " + id));
//
//    //유저의 프로필 이미지 파일 삭제
//    if(employee.getProfile()!=null){
//      fileRepository.deleteById(employee.getProfile().getId());
//    }
//
//    //TODO:
//    employeeRepository.deleteById(id);
//  }
  
  
  
  
  /// 대시보드
  
  
  //상태 필터링 및 입사일 기간 필터링
  public ResponseEntity<Integer> getEmployeeCount(EmployeeStatusType status, LocalDate fromDate,
      LocalDate toDate) {

    Instant fromInstant = (fromDate != null)
        ? fromDate.atStartOfDay(ZoneOffset.UTC).toInstant()
        : Instant.ofEpochMilli(0);

    Instant toInstant = (toDate != null)
        ? toDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant()
        : Instant.now();

    int count = employeeRepository.countEmployees(status, fromInstant, toInstant);
    return ResponseEntity.ok(count);
  }

  public List<EmployeeDistributionResponse> getEmployeeDistribution(String groupBy, EmployeeStatusType status) {
    List<EmployeeDistributionResponse> distributionList;
    long totalEmployees = employeeRepository.countByStatus(status); // 전체 직원 수 조회
    if ("department".equalsIgnoreCase(groupBy)) {
      distributionList = employeeRepository.findEmployeeCountByDepartment(status)
          .stream()
          .map(result -> new EmployeeDistributionResponse(
              result.getGroupName(),
              result.getCount(),
              calculatePercentage(result.getCount(), totalEmployees)
          ))
          .collect(Collectors.toList());

    } else if ("position".equalsIgnoreCase(groupBy)) {
      distributionList = employeeRepository.findEmployeeCountByPosition(status)
          .stream()
          .map(result -> new EmployeeDistributionResponse(
              result.getGroupName(),
              result.getCount(),
              calculatePercentage(result.getCount(), totalEmployees)
          ))
          .collect(Collectors.toList());

    } else {
      throw new IllegalArgumentException("Invalid groupBy value: " + groupBy);
    }

    return distributionList;
  }

  private double calculatePercentage(long count, long total) {
    return (total == 0) ? 0 : (count * 100.0) / total;
  }

  public List<EmployeeTrendResponse> getEmployeeTrend(LocalDate from, LocalDate to, String unit) {
    List<EmployeeTrendResponse> list = new ArrayList<>();
    LocalDate min_LocalDate = LocalDate.of(1970,1,1); //이 기준일 논의 필요?
    Instant min_Instant = min_LocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

    Instant startInstant = from.atStartOfDay().toInstant(ZoneOffset.UTC);
    int beforeCount = employeeRepository.countEmployees(EmployeeStatusType.ACTIVE, min_Instant, startInstant);

    list.add(new EmployeeTrendResponse(from, beforeCount, 0, 0));

    while (!from.isAfter(to)) {
      LocalDate nextFrom;

      switch (unit) {
        case "day":
          nextFrom = from.plusDays(1);
          break;
        case "week":
          nextFrom = from.plusWeeks(1);
          break;
        case "quarter":
          nextFrom = from.plusMonths(3);
          break;
        case "year":
          nextFrom = from.plusYears(1);
          break;
        case "month":
        default:
          nextFrom = from.plusMonths(1);
          break;
      }

      Instant nextInstant = nextFrom.atStartOfDay(ZoneId.systemDefault()).toInstant();
      Instant currentInstant = from.atStartOfDay(ZoneId.systemDefault()).toInstant();

      Integer activeCount = employeeRepository.countEmployees(EmployeeStatusType.ACTIVE, min_Instant, nextInstant);
      Integer onLeaveCount = employeeRepository.countEmployees(EmployeeStatusType.ON_LEAVE, min_Instant, nextInstant);

      int count = (activeCount != null ? activeCount : 0) + (onLeaveCount != null ? onLeaveCount : 0);
      int change = count - beforeCount;
      double percentage = calculatePercentage(count, change, beforeCount);

      list.add(new EmployeeTrendResponse(nextFrom, Math.abs(count), change, percentage));

      beforeCount = count;
      from = nextFrom;
    }
    list.remove(list.size() - 1);

    return list;
  }

  private double calculatePercentage(int count, int change, int beforeCount) {
    if (beforeCount == 0) {
      return count > 0 ? 100.0 : 0.0;
    }
    return ((double) change / beforeCount) * 100.0;
  }


}
