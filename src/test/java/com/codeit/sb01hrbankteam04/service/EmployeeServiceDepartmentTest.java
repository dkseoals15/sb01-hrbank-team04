package com.codeit.sb01hrbankteam04.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.codeit.sb01hrbankteam04.domain.department.Department;
import com.codeit.sb01hrbankteam04.domain.department.DepartmentRepository;
import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.domain.employee.repository.EmployeeRepository;
import com.codeit.sb01hrbankteam04.domain.employee.service.EmployeeService;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeDistributionResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeServiceDepartmentTest {

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
  @BeforeEach
  void setUp() {
    employeeRepository.deleteAll(); // 기존 데이터 초기화
    departmentRepository.deleteAll();

    // 부서 생성
    financeDepartment = createDepartment("Finance", "Handles financial matters", 10);
    backEndDepartment = createDepartment("Back-end", "Handles Back-end matters", 8);
    frontEndDepartment = createDepartment("Front-end", "Handles Front-end matters", 5);
    managementDepartment = createDepartment("Management", "Handles overall management", 12);
    janitorDepartment = createDepartment("Janitor", "Handles cleaning and maintenance", 15);

    // 직원 추가
    addEmployees(financeDepartment, 3, 5, 7);
    addEmployees(backEndDepartment, 2, 5, 6);
    addEmployees(frontEndDepartment, 1, 3, 5);
    addEmployees(managementDepartment, 3, 5, 8);
    addEmployees(janitorDepartment, 1,2,4);
  }

  private Department createDepartment(String name, String description, int yearsOld) {
    Department department = new Department(name, description, LocalDate.now().minusYears(yearsOld).atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
    return departmentRepository.save(department);
  }

  private void addEmployees(Department department, int head, int mid, int small) {
    for (int i = 1; i <= head; i++) {
      Employee employee = new Employee(
          EmployeeStatusType.ACTIVE, // 상태 설정 (예: EmployeeStatusType.재직중, EmployeeStatusType.휴직중 등)
          department.getName() + " Employee부장 " + i, // 이름 설정
          department.getName().toLowerCase() + i + "@부장example.com", // 이메일 설정
          "EMP_" + department.getName().substring(0, 3).toUpperCase() + "_" + i + "_" + System.nanoTime(), // 유니크한 직원 코드 생성
          department, // 부서 설정
          "부장", // 직급 설정
          LocalDate.of(2023, 1, (i % 28) + 1).atStartOfDay().toInstant(java.time.ZoneOffset.UTC), // 입사일 설정
          null // profile은 선택사항으로 null로 설정
      );
      employeeRepository.save(employee);

    }
    for (int i = 1; i <= mid; i++) {
      Employee employee = new Employee(
          EmployeeStatusType.ACTIVE, // 상태 설정 (예: EmployeeStatusType.재직중, EmployeeStatusType.휴직중 등)
          department.getName() + " Employee과장 " + i, // 이름 설정
          department.getName().toLowerCase() + i + "@과장example.com", // 이메일 설정
          "EMP_" + department.getName().substring(0, 3).toUpperCase() + "_" + i + "_" + System.nanoTime(), // 유니크한 직원 코드 생성
          department, // 부서 설정
          "과장", // 직급 설정
          LocalDate.of(2023, 1, (i % 28) + 1).atStartOfDay().toInstant(java.time.ZoneOffset.UTC), // 입사일 설정
          null // profile은 선택사항으로 null로 설정
      );
      employeeRepository.save(employee);

    }
    for (int i = 1; i <= small; i++) {
      Employee employee = new Employee(
          EmployeeStatusType.ACTIVE, // 상태 설정 (예: EmployeeStatusType.재직중, EmployeeStatusType.휴직중 등)
          department.getName() + " Employee대리 " + i, // 이름 설정
          department.getName().toLowerCase() + i + "@대리example.com", // 이메일 설정
          "EMP_" + department.getName().substring(0, 3).toUpperCase() + "_" + i + "_" + System.nanoTime(), // 유니크한 직원 코드 생성
          department, // 부서 설정
          "대리", // 직급 설정
          LocalDate.of(2023, 1, (i % 28) + 1).atStartOfDay().toInstant(java.time.ZoneOffset.UTC), // 입사일 설정
          null // profile은 선택사항으로 null로 설정
      );
      employeeRepository.save(employee);

    }
  }


  @Test
  void 부서별_직원_분포_조회() {
    List<EmployeeDistributionResponse> employeeDistribution = employeeService.getEmployeeDistribution(
        "department", EmployeeStatusType.ACTIVE);

    // 기대되는 부서별 직원 수 및 비율
    Map<String, EmployeeDistributionResponse> expected = Map.of(
        "Finance", new EmployeeDistributionResponse("Finance", 15, 25),
        "Back-end", new EmployeeDistributionResponse("Back-end", 13, 21.6),
        "Front-end", new EmployeeDistributionResponse("Front-end", 9, 15.0),
        "Management", new EmployeeDistributionResponse("Management", 16, 26.6),
        "Janitor", new EmployeeDistributionResponse("Janitor", 7, 11.6)
    );

    // 검증: 반환된 리스트의 크기가 5개인지 확인
    assertThat(employeeDistribution).hasSize(5);

    // 부서별 count 및 percentage 검증
    for (EmployeeDistributionResponse response : employeeDistribution) {
      String groupName = response.getGroupKey();
      System.out.println("groupName = " + groupName);
      assertThat(expected).containsKey(groupName);

      EmployeeDistributionResponse expectedResponse = expected.get(groupName);
      assertThat(response.getCount()).isEqualTo(expectedResponse.getCount());
      assertThat(Math.floor(response.getPercentage() * 10.0) / 10.0).isEqualTo(expectedResponse.getPercentage());
    }
    // 콘솔 출력 (디버깅용)
    System.out.println("employeeDistribution = " + employeeDistribution);
  }


}
