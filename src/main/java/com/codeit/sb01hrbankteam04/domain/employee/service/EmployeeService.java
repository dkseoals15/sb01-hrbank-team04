package com.codeit.sb01hrbankteam04.domain.employee.service;

import com.codeit.sb01hrbankteam04.domain.department.entity.Department;
import com.codeit.sb01hrbankteam04.domain.department.repository.DepartmentRepository;
import com.codeit.sb01hrbankteam04.domain.department.service.DepartmentService;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeDistributionResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeePageResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeTrendResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeUpdateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.domain.employee.mapper.EmployeeMapper;
import com.codeit.sb01hrbankteam04.domain.employee.repository.EmployeeRepository;
import com.codeit.sb01hrbankteam04.domain.file.dto.FileDto;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import com.codeit.sb01hrbankteam04.domain.file.entity.FileType;
import com.codeit.sb01hrbankteam04.domain.file.mapper.FileMapper;
import com.codeit.sb01hrbankteam04.domain.file.repository.FileRepository;
import com.codeit.sb01hrbankteam04.domain.file.service.FileService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.management.InstanceAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Transactional
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final FileRepository fileRepository;
  private final EmployeeMapper employeeMapper;
  private final FileService fileService;
  private final FileMapper fileMapper;
  private final DepartmentService departmentService;

  /// 직원 관리 CRUD

  /**
   * 직원 등록 기능-등록된 프로필 파일을 함게 저장합니다.
   *
   * @param employeeCreateRequest  직원생성요청 Dto
   * @param optionalProfileRequest 프로필 이미지(선택적)
   * @return 직원응답 Dto
   */
  @Transactional
  public EmployeeResponse create(EmployeeCreateRequest employeeCreateRequest,
      MultipartFile optionalProfileRequest) throws IOException, InstanceAlreadyExistsException {

    //부서 찾기
    Department department = departmentRepository.findById(employeeCreateRequest.departmentId())
        .orElseThrow(() -> new NoSuchElementException("Department not found"));

    //파일 저장
    FileDto fileDto = null;
    File nullableProfile =null;
    
    //파일이 있을 때 파일 create 및 파일 설정
    if(optionalProfileRequest != null) {
      fileDto = fileService.create(optionalProfileRequest, FileType.PROFILE);
      nullableProfile = fileRepository.findById(fileDto.id()).orElse(null);
    }
    
    Employee employee = Employee.builder()
        .status(EmployeeStatusType.ACTIVE)
        .name(employeeCreateRequest.name())
        .email(employeeCreateRequest.email())
        .department(department)
        .position(employeeCreateRequest.position())
        .joinedAt(dateToInstant(employeeCreateRequest.hireDate()))
        .profile(nullableProfile)
        .build();

    employeeRepository.save(employee);
    return employeeMapper.toDto(employee);
  }


  /**
   * 문자열-> Intant 변환 기능- "yyyy-MM-dd" 형식의 String을 Instant로 변환합니다.
   *
   * @param date "yyyy-MM-dd" 꼴 string
   * @return 변환된 Instant 객체
   */
  private Instant dateToInstant(String date) {
    if (date == null) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //문자열을 localDate 변환
    LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    //LocalDate를 Instant 형식으로 변환, (00:00 UT
    Instant instant = localDate.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant();
    return instant;
  }

  /**
   * 직원 조회 기능
   *
   * @param nameOrEmail    이름 or 이메일
   * @param employeeNumber 사번
   * @param departmentName 부서이름
   * @param position       직함
   * @param hireDateFrom   입사일(조회시작)
   * @param hireDateTo     입사일(조회끝)
   * @param status         상태
   * @param nextIdAfter    다음 ID
   * @param cursor         커서
   * @param size           페이지 사이즈
   * @param sortBy         정렬기준
   * @param sortDirection  정렬방법
   * @return 직원페이지응답 Dto
   */

  public EmployeePageResponse getEmployees(
      String nameOrEmail, String employeeNumber, String departmentName, String position,
      String hireDateFrom, String hireDateTo, String status,
      Long nextIdAfter, String cursor, int size, String sortBy, String sortDirection) {

    // Pageable 객체를 생성
    Pageable pageable = PageRequest.of(0, size);

    //List 형태로 Page 받기
    List<Employee> employees = employeeRepository.findEmployeesByCursor(
        nameOrEmail, employeeNumber, departmentName, position,
        hireDateFrom, hireDateTo, status, cursor, sortBy, sortDirection, pageable);

    // pageResponse  dto로 변환
    List<EmployeeResponse> EmployeeResponses = employees.stream()
        .map(employeeMapper::toDto)
        .collect(Collectors.toList());


    //Long totalElements = 1L;
    Long totalElements = employeeRepository.countPageTotalCount(
        nameOrEmail, employeeNumber, departmentName, position,
        hireDateFrom, hireDateTo, status );

    //현재 페이지의 목록 크기
    int pageSize = employees.size();

    //nextIdAfter, 현 employees의 마지막 직원의 id
    Long lastId = null;
    if(size - pageSize > 0){
      //null 맞음
    }
    else if(size - pageSize==0){
      lastId = totalElements ==size ? null :employees.get(employees.size() - 1).getId();
    }
    
    //넥스트커서로, 분류기준에 따라 값을 가져야함.
    String nextCursor = lastId != null ? convertCursor(sortBy, employees.get(employees.size() - 1) ) : null;
    boolean hasNext = employees.size() == size;



    return  EmployeePageResponse.from(
        EmployeeResponses, nextCursor, lastId, pageSize, totalElements, hasNext);
  }

  // 커서의 값을 반환한다.
  private String convertCursor(String sortBy, Employee employee) {
    if(sortBy.equals("name")){
      return employee.getName();
    }
    else if(sortBy.equals("hireDate")){
      return DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(employee.getJoinedAt());
    }
    else return  employee.getCode();
  }

  /**
   * 직원 상세 조회
   *
   * @param id 조회할 직원 아이디
   * @return 직원 응답 Dto
   */

  public EmployeeResponse find(Long id) {
    return employeeRepository.findById(id)
        .map(employeeMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("no such employee with id " + id));
  }

  /**
   * 직원 업데이트
   *
   * @param id                     업데이트할 직원 아이디
   * @param employeeUpdateRequest  직원 업데이트 요청 Dto
   * @param optionalProfileRequest 등록할 이미지(선택적)
   * @return 직원응답 Dto
   */
  @Transactional
  public EmployeeResponse update(Long id,
      EmployeeUpdateRequest employeeUpdateRequest,
      MultipartFile optionalProfileRequest)
      throws IOException {

    Employee employee = employeeRepository.findById(id).orElseThrow();
    FileDto newFile = null;

    //직원 현 정보-request 정보 비교하여 업데이트
    updateEmployeeInfo(employee, employeeUpdateRequest);

    if (optionalProfileRequest != null) {
      // 프로필 업데이트가 필요
      if (employee.getProfile() != null) {
        // 프로필 존재 시 삭제
        fileService.delete(employee.getProfile());
      }
      newFile = fileService.create(optionalProfileRequest, FileType.PROFILE);

      // TODO: Exception
      employee.setProfile(fileRepository.findById(newFile.id()).orElseThrow());
    }
    return employeeMapper.toDto(employee);
  }

  /**
   * 직원 정보 업데이트
   */
  private void updateEmployeeInfo(Employee current, EmployeeUpdateRequest updateDto) {

    // name
    if (!current.getName().equals(updateDto.name())) {
      current.setName(updateDto.name());
    }
    // email
    if (!current.getName().equals(updateDto.name()) && employeeRepository.existsByEmail(
        updateDto.email())) {
      current.setEmail(updateDto.email());
    }
    // dept
    if (!current.getDepartment().getId().equals(updateDto.departmentId())) {
      // TODO: Exception
      current.setDepartment(departmentRepository.findById(updateDto.departmentId()).orElseThrow());
    }
    // positon
    if (!current.getPosition().equals(updateDto.position())) {
      current.setPosition(updateDto.position());
    }
    // join date
    if (!current.getJoinedAt().equals(dateToInstant(updateDto.hireDate()))) {
      current.setJoinedAt(dateToInstant(updateDto.hireDate()));
    }
    // status
    if (!current.getStatus().equals(EmployeeStatusType.valueOf(updateDto.status()))) {
      current.setStatus(EmployeeStatusType.valueOf(updateDto.status()));
    }
  }

  /**
   * 직원 삭제 기능-등록된 유저의 이미지 파일도 함께 삭제합니다.
   *
   * @param id 삭제할 직원의 아이디
   */
  @Transactional
  public void delete(Long id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("no such employee with id " + id));

    //유저의 프로필 이미지 파일 삭제
    if (employee.getProfile() != null) {
      fileRepository.deleteById(employee.getProfile().getId());
    }

    employeeRepository.deleteById(id);
  }

  //대시보드

  /**
   * 상태 필터링 및 입사일 기간 필터링
   *
   * @param { status 직원의 상태 값,  fromDate 입사일 계산 시작 날짜, toDate 입사일 계산 끝 날짜 }
   * @return 필터링된 직원의수
   */
  public ResponseEntity<Integer> getEmployeeCount(EmployeeStatusType status, LocalDate fromDate,
      LocalDate toDate) {

    Instant fromInstant = (fromDate != null)
        ? fromDate.atStartOfDay(ZoneOffset.UTC).toInstant()
        : Instant.ofEpochMilli(0);

    Instant toInstant = (toDate != null)
        ? toDate.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant()
        : Instant.now();

    //해당 기간 동안 직원 수 카운팅
    int count = employeeRepository.countEmployees(status, fromInstant, toInstant);
    return ResponseEntity.ok(count);
  }

  /**
   * 그룹과 상태에 따른 필터링
   *
   * @param {groupBy 필터링 검색 옵션,status 직원 상태 값}
   * @return 검색된 각 그룹의 이름, 인원 수, 퍼샌트
   */
  public List<EmployeeDistributionResponse> getEmployeeDistribution(String groupBy,
      EmployeeStatusType status) {
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

  /**
   * 단위 기간 설정에 따른 필터링
   *
   * @param {from 검색 시작 날짜 to 검색 끝 날짜 unit 단위 기간 설정}
   * @return 단위 기간 당 직원 수를 나타내는 리스트
   */
  public List<EmployeeTrendResponse> getEmployeeTrend(LocalDate from, LocalDate to, String unit) {
    List<EmployeeTrendResponse> list = new ArrayList<>();
    LocalDate min_LocalDate = LocalDate.of(1970, 1, 1); //POSIX 시간
    Instant min_Instant = min_LocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

    Instant startInstant = from.atStartOfDay().toInstant(ZoneOffset.UTC);
    int beforeCount = employeeRepository.countEmployees(EmployeeStatusType.ACTIVE, min_Instant,
        startInstant);

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

      Integer activeCount = employeeRepository.countEmployees(EmployeeStatusType.ACTIVE,
          min_Instant, nextInstant);
      Integer onLeaveCount = employeeRepository.countEmployees(EmployeeStatusType.ON_LEAVE,
          min_Instant, nextInstant);

      int count =
          (activeCount != null ? activeCount : 0) + (onLeaveCount != null ? onLeaveCount : 0);
      int change = count - beforeCount;
      double percentage = calculatePercentage(count, change, beforeCount);

      percentage = Math.floor(percentage * 10) / 10; //0320 양병운 수정

      list.add(new EmployeeTrendResponse(nextFrom, Math.abs(count), change, percentage));

      beforeCount = count;
      from = nextFrom;
    }
    list.remove(list.size() - 1);

    return list;
  }

  /*
   * 퍼센트 계산
   * */
  private double calculatePercentage(int count, int change, int beforeCount) {
    if (beforeCount == 0) {
      return count > 0 ? 100.0 : 0.0;
    }
    return ((double) change / beforeCount) * 100.0;
  }
}
