package com.codeit.sb01hrbankteam04.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.domain.employee.repository.EmployeeRepository;
import com.codeit.sb01hrbankteam04.domain.employee.service.EmployeeService;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeDistributionResponse;
import com.codeit.sb01hrbankteam04.domain.department.Department;
import com.codeit.sb01hrbankteam04.repository.DepartmentRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class EmployeeServicePositionTest {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

  private Department financeDepartment;
  private Department backEndDepartment;
  private Department frontEndDepartment;
  private Department managementDepartment;
  private Department janitorDepartment;

  private Department createDepartment(String name, String description, int yearsOld) {
    Department department = new Department();
    department.setName(name);
    department.setDescription(description);
    department.setEstablishedAt(
        LocalDate.now().minusYears(yearsOld).atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
    return departmentRepository.save(department);
  }

  @BeforeEach
  void setUp() {
    employeeRepository.deleteAll(); // 기존 데이터 초기화
    departmentRepository.deleteAll();

    // ✅ 부서 생성
    financeDepartment = createDepartment("Finance", "Handles financial matters", 10);
    backEndDepartment = createDepartment("Back-end", "Handles Back-end matters", 8);
    frontEndDepartment = createDepartment("Front-end", "Handles Front-end matters", 5);
    managementDepartment = createDepartment("Management", "Handles overall management", 12);
    janitorDepartment = createDepartment("Janitor", "Handles cleaning and maintenance", 15);

    // ✅ 정확한 직급별 직원 수 배정
    addEmployees(financeDepartment, 2, 4, 6, 3);  // 부장 2, 과장 4, 대리 6, 사원 1
    addEmployees(backEndDepartment, 2, 3, 5, 4); // 부장 2, 과장 3, 대리 5, 사원 2
    addEmployees(frontEndDepartment, 2, 3, 4, 3); // 부장 2, 과장 3, 대리 4, 사원 1
    addEmployees(managementDepartment, 2, 3, 4, 4); // 부장 2, 과장 3, 대리 4, 사원 2
    addEmployees(janitorDepartment, 2, 2, 1, 1);  // 부장 2, 과장 2, 대리 1, 사원 4
  }


  private void addEmployees(Department department, int head, int mid, int small, int entry) {
    addEmployeesByPosition(department, "부장", head);
    addEmployeesByPosition(department, "과장", mid);
    addEmployeesByPosition(department, "대리", small);
    addEmployeesByPosition(department, "사원", entry);
  }

  private void addEmployeesByPosition(Department department, String position, int count) {
    for (int i = 1; i <= count; i++) {
      Employee employee = new Employee(
          EmployeeStatusType.재직중, // 상태 설정 (예: EmployeeStatusType.재직중, 휴직중 등)
          position + " " + department.getName() + " Employee " + i, // 이름 설정
          position.toLowerCase() + "." + department.getName().toLowerCase() + i + "@example.com",
          // 이메일 설정
          "EMP_" + position.substring(0, 2).toUpperCase() + "_" + i + "_" + System.nanoTime(),
          // 유니크한 직원 코드 생성
          department, // 부서 설정
          position, // 직급 설정
          LocalDate.of(2023, 1, (i % 28) + 1).atStartOfDay().toInstant(java.time.ZoneOffset.UTC),
          // 입사일 설정
          null // profile은 선택사항으로 null로 설정
      );
      employeeRepository.save(employee);

    }

    for (int i = 1; i <= 5; i++) {
      Employee employee = new Employee(
          EmployeeStatusType.휴직중, // 상태 설정 (예: EmployeeStatusType.휴직중, 재직중 등)
          position + "2 " + department.getName() + " Employee " + i, // 이름 설정
          position.toLowerCase() + "2." + department.getName().toLowerCase() + i + "@example.com",
          // 이메일 설정
          "EMP_" + position.substring(0, 2).toUpperCase() + "2_" + i + "_" + System.nanoTime(),
          // 유니크한 직원 코드 생성
          department, // 부서 설정
          position, // 직급 설정
          LocalDate.of(2023, 1, (i % 28) + 1).atStartOfDay().toInstant(java.time.ZoneOffset.UTC),
          // 입사일 설정
          null // profile은 선택사항으로 null로 설정
      );
      employeeRepository.save(employee);

    }

  }

  @Test
  void 직함별_직원_분포_조회() {
    // ✅ position 기준 직원 분포 조회
    List<EmployeeDistributionResponse> positionDistribution = employeeService.getEmployeeDistribution(
        "position", EmployeeStatusType.재직중);

    // ✅ 기대되는 직급별 직원 수 및 비율 (정확한 직원 수 반영)
    Map<String, EmployeeDistributionResponse> expectedPosition = Map.of(
        "부장", new EmployeeDistributionResponse("부장", 10, 16.0),
        "과장", new EmployeeDistributionResponse("과장", 15, 25.0),
        "대리", new EmployeeDistributionResponse("대리", 20, 33.0),
        "사원", new EmployeeDistributionResponse("사원", 15, 25.0)
    );

    // ✅ 검증: 4개 직급(부장, 과장, 대리, 사원)만 존재하는지 확인
    assertThat(positionDistribution).hasSize(4);

    // ✅ 검증: 각 직급별 직원 수 및 비율 확인
    for (EmployeeDistributionResponse response : positionDistribution) {
      String groupName = response.getGroupKey();
      assertThat(expectedPosition).containsKey(groupName);

      EmployeeDistributionResponse expectedResponse = expectedPosition.get(groupName);
      assertThat(response.getCount()).isEqualTo(expectedResponse.getCount());

      // ✅ 소수점 두 자리 이하 버리고 정수형 비교
      int actualPercentage = (int) response.getPercentage();
      int expectedPercentage = (int) expectedResponse.getPercentage();

      assertThat(actualPercentage).isEqualTo(expectedPercentage);

    }
  }

  @Test
  public void 상태별_직원_분포_조회() {
    // ✅ position 기준 직원 분포 조회
    List<EmployeeDistributionResponse> positionDistribution2 = employeeService.getEmployeeDistribution(
        "position", EmployeeStatusType.휴직중);

    // ✅ 기대되는 직급별 직원 수 및 비율 (정확한 직원 수 반영)
    Map<String, EmployeeDistributionResponse> expectedPosition2 = Map.of(
        "부장", new EmployeeDistributionResponse("부장", 25, 25),
        "과장", new EmployeeDistributionResponse("과장", 25, 25),
        "대리", new EmployeeDistributionResponse("대리", 25, 25),
        "사원", new EmployeeDistributionResponse("사원", 25, 25)
    );

    // ✅ 검증: 4개 직급(부장, 과장, 대리, 사원)만 존재하는지 확인
    assertThat(positionDistribution2).hasSize(4);

    // ✅ 검증: 각 직급별 직원 수 및 비율 확인
    for (EmployeeDistributionResponse response2 : positionDistribution2) {
      String groupName2 = response2.getGroupKey();
      assertThat(expectedPosition2).containsKey(groupName2);

      EmployeeDistributionResponse expectedResponse2 = expectedPosition2.get(groupName2);
      assertThat(response2.getCount()).isEqualTo(expectedResponse2.getCount());

      // ✅ 소수점 두 자리 이하 버리고 정수형 비교
      int actualPercentage2 = (int) response2.getPercentage();
      int expectedPercentage2 = (int) expectedResponse2.getPercentage();

      assertThat(actualPercentage2).isEqualTo(expectedPercentage2);

    }

    // ✅ 콘솔 출력 (디버깅용)
    System.out.println("positionDistribution2 = " + positionDistribution2);
  }

  @Test
  public void 전체_직원_수() {
    ResponseEntity<Integer> employeeCount = employeeService.getEmployeeCount(null, null, null);
    assertThat(employeeCount.getBody()).isEqualTo(160); // ON_LEAVE - 100, ACTIVE - 60
  }
  @Test
  public void ON_LEAVE_직원_수(){
    ResponseEntity<Integer> employeeCount = employeeService.getEmployeeCount(Status.ON_LEAVE, null, null);
    assertThat(employeeCount.getBody()).isEqualTo(100); // ON_LEAVE - 100, ACTIVE - 60
  }

}
