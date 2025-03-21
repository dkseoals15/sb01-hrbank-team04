package com.codeit.sb01hrbankteam04.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.codeit.sb01hrbankteam04.domain.department.entity.Department;
import com.codeit.sb01hrbankteam04.domain.department.repository.DepartmentRepository;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.domain.employee.repository.EmployeeRepository;
import com.codeit.sb01hrbankteam04.domain.employee.service.EmployeeService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class EmployeeServiceGetEmployeeCountTest {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

  private Department department;

  @BeforeEach
  void setUp() {
    employeeRepository.deleteAll(); // 기존 데이터 초기화
    departmentRepository.deleteAll();

    department = new Department("Finance", "Handles financial matters",
        LocalDate.now().minusYears(10).atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
    department = departmentRepository.save(department);

//    Employee employee1 = new Employee(EmployeeStatusType.ACTIVE, "John Doe", "john.doe@example.com",
//        "EMP123", department, "부장",
//        LocalDate.of(2023, 1, 10).atStartOfDay().toInstant(java.time.ZoneOffset.UTC), null);
//    employeeRepository.save(employee1);
//
//    Employee employee2 = new Employee(EmployeeStatusType.ON_LEAVE, "Jane Smith",
//        "jane.smith@example.com", "EMP456", department, "부장",
//        LocalDate.of(2024, 2, 15).atStartOfDay().toInstant(java.time.ZoneOffset.UTC), null);
//    employeeRepository.save(employee2);
//
//    Employee employee3 = new Employee(EmployeeStatusType.ON_LEAVE, "Jane2 Smith",
//        "jane2.smith@example.com", "EMP4561", department, "부장",
//        LocalDate.of(2023, 3, 15).atStartOfDay().toInstant(java.time.ZoneOffset.UTC), null);
//    employeeRepository.save(employee3);

  }

  @Test
  void testCountEmployees_All() {
    ResponseEntity<Integer> employeeCount = employeeService.getEmployeeCount(null, null, null);
    assertThat(employeeCount.getBody()).isEqualTo(3);
  }

  @Test
  void testCountEmployees_ByStatus() {
    ResponseEntity<Integer> employeeCount = employeeService.getEmployeeCount(
        EmployeeStatusType.ACTIVE, null,
        null);
    assertThat(employeeCount.getBody()).isEqualTo(1);
  }

  @Test
  void testCountEmployees_ByDateRange() {
    ResponseEntity<Integer> employeeCount = employeeService.getEmployeeCount(null,
        LocalDate.of(2023, 3, 1), LocalDate.of(2023, 12, 31));
    assertThat(employeeCount.getBody()).isEqualTo(1);
    ResponseEntity<Integer> employeeCount2 = employeeService.getEmployeeCount(null, null,
        LocalDate.of(2023, 12, 31));
    assertThat(employeeCount2.getBody()).isEqualTo(2);
    ResponseEntity<Integer> employeeCount3 = employeeService.getEmployeeCount(null,
        LocalDate.of(2023, 3, 1), null);
    assertThat(employeeCount3.getBody()).isEqualTo(2);
  }
}
