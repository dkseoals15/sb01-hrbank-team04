package com.codeit.sb01hrbankteam04.service;

import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.domain.employee.repository.EmployeeRepository;
import com.codeit.sb01hrbankteam04.domain.employee.service.EmployeeService;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeTrendResponse;
import com.codeit.sb01hrbankteam04.domain.department.Department;
import com.codeit.sb01hrbankteam04.repository.DepartmentRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeServiceTrendTest {

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

  //부서 생성
  private Department createDepartment(String name, String description, int yearsOld) {
    Department department = new Department();
    department.setName(name);
    department.setDescription(description);
    department.setEstablishedAt(
        LocalDate.now().minusYears(yearsOld).atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
    return departmentRepository.save(department);
  }

  // 퇴사한 직원 상태를 RESIGNED로 변경
  private void updateResignedEmployeeStatus(Employee employee) {
    
    employee.setStatus(EmployeeStatusType.휴직중);

    Instant resignedAt = LocalDate.of(2023, 4, 30).atStartOfDay(ZoneOffset.UTC).toInstant();

    employeeRepository.save(employee);  // 상태 변경 후 저장
  }

  //직원 추가
  private Employee addEmployee(String position, Department department, String email, int year,
      int month, int day, EmployeeStatusType status) {
    Employee employee = new Employee(
        status, // 예: EmployeeStatusType.재직중, EmployeeStatusType.휴직중 등
        position + " " + department.getName() + " Employee", // 이름 설정
        email,
        "EMP_" + position.substring(0, 2).toUpperCase() + "_" + System.nanoTime(), // 코드 설정
        department,
        position, // 직위 설정
        LocalDate.of(year, month, day).atStartOfDay().toInstant(ZoneOffset.UTC), // 입사일 설정
        null // profile은 선택사항으로 null로 설정
    );
    employeeRepository.save(employee);

    return employee;
  }
  
  @BeforeEach
  void setUp() {
    employeeRepository.deleteAll();
    departmentRepository.deleteAll();

    financeDepartment = createDepartment("Finance", "Handles financial matters", 10);
    backEndDepartment = createDepartment("Back-end", "Handles Back-end matters", 8);
    frontEndDepartment = createDepartment("Front-end", "Handles Front-end matters", 5);
    managementDepartment = createDepartment("Management", "Handles overall management", 12);
    janitorDepartment = createDepartment("Janitor", "Handles cleaning and maintenance", 15);

    Employee employee1 = addEmployee("부장", financeDepartment, "finance1@example.com", 2023, 1, 5,
        EmployeeStatusType.재직중);
    addEmployee("부장", financeDepartment, "finance2@example.com", 2023, 12, 10, EmployeeStatusType.재직중);

    Employee employee2 = addEmployee("과장", backEndDepartment, "backend1@example.com", 2023, 3, 12,
        EmployeeStatusType.재직중);
    addEmployee("과장", backEndDepartment, "backend2@example.com", 2023, 4, 15, EmployeeStatusType.재직중);
    addEmployee("과장", backEndDepartment, "backend3@example.com", 2023, 5, 18, EmployeeStatusType.재직중);

    addEmployee("대리", frontEndDepartment, "frontend1@example.com", 2023, 6, 22, EmployeeStatusType.재직중);
    addEmployee("대리", frontEndDepartment, "frontend2@example.com", 2023, 7, 25, EmployeeStatusType.재직중);
    addEmployee("대리", frontEndDepartment, "frontend3@example.com", 2023, 8, 28, EmployeeStatusType.재직중);

    addEmployee("사원", managementDepartment, "management1@example.com", 2023, 9, 1, EmployeeStatusType.재직중);
    addEmployee("사원", managementDepartment, "management2@example.com", 2023, 10, 3, EmployeeStatusType.재직중);

    addEmployee("부장", janitorDepartment, "janitor1@example.com", 2023, 11, 5, EmployeeStatusType.재직중);
    addEmployee("부장", janitorDepartment, "janitor2@example.com", 2023, 12, 8, EmployeeStatusType.재직중);

    addEmployee("과장", financeDepartment, "finance3@example.com", 2023, 1, 7, EmployeeStatusType.재직중);
    addEmployee("과장", financeDepartment, "finance4@example.com", 2023, 12, 12, EmployeeStatusType.재직중);

    Employee employee3 = addEmployee("대리", backEndDepartment, "backend4@example.com", 2023, 3, 15,
        EmployeeStatusType.재직중);
    addEmployee("대리", backEndDepartment, "backend5@example.com", 2023, 4, 20, EmployeeStatusType.재직중);

    addEmployee("사원", frontEndDepartment, "frontend4@example.com", 2023, 5, 23, EmployeeStatusType.재직중);
    addEmployee("사원", frontEndDepartment, "frontend5@example.com", 2023, 6, 26, EmployeeStatusType.재직중);

    addEmployee("부장", managementDepartment, "management3@example.com", 2023, 7, 29, EmployeeStatusType.재직중);
    addEmployee("부장", managementDepartment, "management4@example.com", 2023, 8, 5, EmployeeStatusType.재직중);

    addEmployee("과장", janitorDepartment, "janitor3@example.com", 2023, 9, 9, EmployeeStatusType.재직중);
    addEmployee("과장", janitorDepartment, "janitor4@example.com", 2023, 10, 14, EmployeeStatusType.재직중);

    addEmployee("대리", financeDepartment, "finance5@example.com", 2023, 11, 17, EmployeeStatusType.재직중);
    addEmployee("대리", financeDepartment, "finance6@example.com", 2023, 12, 21, EmployeeStatusType.재직중);

    addEmployee("사원", backEndDepartment, "backend6@example.com", 2023, 1, 24, EmployeeStatusType.재직중);
    addEmployee("사원", backEndDepartment, "backend7@example.com", 2023, 12, 27, EmployeeStatusType.재직중);
    Employee employee4 = addEmployee("사원", backEndDepartment, "backend8@example.com", 2023, 3, 27,
        EmployeeStatusType.재직중);
    
    //사직 시 처리되는 지 확인
    updateResignedEmployeeStatus(employee1);
    updateResignedEmployeeStatus(employee2);
    updateResignedEmployeeStatus(employee3);
    updateResignedEmployeeStatus(employee4);


  }

  @Test
  public void 직원_수_추이_조회() {
    List<Employee> all = employeeRepository.findAll();
    for (Employee employee : all) {
      System.out.println("employee = " + employee);
      System.out.println(employee.getJoinedAt()
          .equals(LocalDate.of(2023, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant()));
    }
    List<EmployeeTrendResponse> list = employeeService.getEmployeeTrend(
        LocalDate.of(2023, 1, 1),
        LocalDate.of(2023, 12, 31),
        "week");
    for (EmployeeTrendResponse employeeTrendResponse : list) {
      System.out.println(employeeTrendResponse);
    }
  }
}