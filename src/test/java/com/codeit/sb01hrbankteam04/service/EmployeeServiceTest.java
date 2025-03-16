package com.codeit.sb01hrbankteam04.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.codeit.sb01hrbankteam04.model.Department;
import com.codeit.sb01hrbankteam04.model.Employee;
import com.codeit.sb01hrbankteam04.model.Status;
import com.codeit.sb01hrbankteam04.repository.DepartmentRepository;
import com.codeit.sb01hrbankteam04.repository.EmployeeRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class EmployeeServiceTest {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

  private Department department;

  @BeforeEach
  void setUp() {
    department = new Department();
    department.setName("Finance");
    department.setDescription("Handles financial matters");
    department.setEstablishedAt(
        LocalDate.now().minusYears(10).atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
    department = departmentRepository.save(department);

    Employee employee1 = new Employee();
    employee1.setName("John Doe");
    employee1.setEmail("john.doe@example.com");
    employee1.setCode("EMP123");
    employee1.setStatus(Status.ACTIVE);
    employee1.setDepartment(department);
    employee1.setJoinedAt(
        LocalDate.of(2023, 1, 10).atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
    employeeRepository.save(employee1);

    Employee employee2 = new Employee();
    employee2.setName("Jane Smith");
    employee2.setEmail("jane.smith@example.com");
    employee2.setCode("EMP456");
    employee2.setStatus(Status.ON_LEAVE);
    employee2.setDepartment(department);
    employee2.setJoinedAt(
        LocalDate.of(2024, 2, 15).atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
    employeeRepository.save(employee2);

    Employee employee3 = new Employee();
    employee3.setName("Jane2 Smith");
    employee3.setEmail("jane2.smith@example.com");
    employee3.setCode("EMP4561");
    employee3.setStatus(Status.ON_LEAVE);
    employee3.setDepartment(department);
    employee3.setJoinedAt(
        LocalDate.of(2023, 3, 15).atStartOfDay().toInstant(java.time.ZoneOffset.UTC));
    employeeRepository.save(employee3);
  }

  @Test
  void testCountEmployees_All() {
    ResponseEntity<Integer> employeeCount = employeeService.getEmployeeCount(null, null, null);
    assertThat(employeeCount.getBody()).isEqualTo(3);
  }

  @Test
  void testCountEmployees_ByStatus() {
    ResponseEntity<Integer> employeeCount = employeeService.getEmployeeCount(Status.ACTIVE, null,
        null);
    assertThat(employeeCount.getBody()).isEqualTo(1);
  }

  @Test
  void testCountEmployees_ByDateRange() {
    ResponseEntity<Integer> employeeCount = employeeService.getEmployeeCount(null, LocalDate.of(2023, 3, 1), LocalDate.of(2023, 12, 31));
    assertThat(employeeCount.getBody()).isEqualTo(1);
    ResponseEntity<Integer> employeeCount2 = employeeService.getEmployeeCount(null, null, LocalDate.of(2023, 12, 31));
    assertThat(employeeCount2.getBody()).isEqualTo(2);
    ResponseEntity<Integer> employeeCount3 = employeeService.getEmployeeCount(null, LocalDate.of(2023, 3, 1), null);
    assertThat(employeeCount3.getBody()).isEqualTo(2);
  }
}
