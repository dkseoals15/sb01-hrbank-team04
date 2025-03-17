package com.codeit.sb01hrbankteam04.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.codeit.sb01hrbankteam04.dto.employee.EmployeeDistributionResponse;
import com.codeit.sb01hrbankteam04.model.Department;
import com.codeit.sb01hrbankteam04.model.Employee;
import com.codeit.sb01hrbankteam04.model.Status;
import com.codeit.sb01hrbankteam04.repository.DepartmentRepository;
import com.codeit.sb01hrbankteam04.repository.EmployeeRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeServiceTest {

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
    addEmployees(financeDepartment, 7);
    addEmployees(backEndDepartment, 12);
    addEmployees(frontEndDepartment, 5);
    addEmployees(managementDepartment, 13);
    addEmployees(janitorDepartment, 13);
  }

  private Department createDepartment(String name, String description, int yearsOld) {
    Department department = new Department();
    department.setName(name);
    department.setDescription(description);
    department.setEstablishedAt(
        LocalDate.now().minusYears(yearsOld).atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
    return departmentRepository.save(department);
  }

  private void addEmployees(Department department, int count) {
    for (int i = 1; i <= count; i++) {
      Employee employee = new Employee();
      employee.setName(department.getName() + " Employee " + i);
      employee.setEmail(department.getName().toLowerCase() + i + "@example.com");

      // 중복 방지를 위한 유니크한 직원 코드 생성
      employee.setCode("EMP_" + department.getName().substring(0, 3).toUpperCase() + "_" + i + "_" + System.nanoTime());

      employee.setStatus(Status.ACTIVE);
      employee.setDepartment(department);
      employee.setJoinedAt(
          LocalDate.of(2023, 1, (i % 28) + 1).atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
      employeeRepository.save(employee);
    }
  }


  @Test
  void 직원_분포_조회() {
    // 직원 분포 조회
    List<EmployeeDistributionResponse> employeeDistribution = employeeService.getEmployeeDistribution(
        "department", Status.ACTIVE);

    // 기대되는 부서별 직원 수 및 비율
    Map<String, EmployeeDistributionResponse> expected = Map.of(
        "Finance", new EmployeeDistributionResponse("Finance", 7, 14.0),
        "Back-end", new EmployeeDistributionResponse("Back-end", 12, 24.0),
        "Front-end", new EmployeeDistributionResponse("Front-end", 5, 10.0),
        "Management", new EmployeeDistributionResponse("Management", 13, 26.0),
        "Janitor", new EmployeeDistributionResponse("Janitor", 13, 26.0)
    );

    // 검증: 반환된 리스트의 크기가 5개인지 확인
    assertThat(employeeDistribution).hasSize(5);

    // 부서별 count 및 percentage 검증
    for (EmployeeDistributionResponse response : employeeDistribution) {
      String groupName = response.getGroupKey();
      assertThat(expected).containsKey(groupName);

      EmployeeDistributionResponse expectedResponse = expected.get(groupName);
      assertThat(response.getCount()).isEqualTo(expectedResponse.getCount());
      assertThat(response.getPercentage()).isEqualTo(expectedResponse.getPercentage());
    }

    // 콘솔 출력 (디버깅용)
    System.out.println("employeeDistribution = " + employeeDistribution);
  }

}
