package com.codeit.sb01hrbankteam04.repository.employee;

import com.codeit.sb01hrbankteam04.model.Department;
import com.codeit.sb01hrbankteam04.model.Employee;
import com.codeit.sb01hrbankteam04.model.Status;
import com.codeit.sb01hrbankteam04.repository.DepartmentRepository;
import com.codeit.sb01hrbankteam04.repository.EmployeeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EmployeeRepositoryInitTest {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

  @PersistenceContext
  private EntityManager entityManager;

  private Department department;

  @BeforeEach
  void setUp() {
    employeeRepository.deleteAll();
    departmentRepository.deleteAll();

    department = new Department();
    department.setName("Finance");
    department.setDescription("Handles financial matters");
    department.setEstablishedAt(Instant.now());
    department = departmentRepository.save(department);
  }

  @Test
  void 직원_저장() {
    Employee employee = new Employee();
    employee.setName("John Doe");
    employee.setEmail("john.doe@example.com");
    employee.setCode("EMP12345");
    employee.setStatus(Status.ACTIVE);
    employee.setDepartment(department);
    employee.setPosition("Manager");
    employee.setJoinedAt(Instant.now());

    Employee savedEmployee = employeeRepository.save(employee);

    assertThat(savedEmployee.getId()).isNotNull();
    assertThat(savedEmployee.getName()).isEqualTo("John Doe");
  }

  @Test
  void 이메일로_직원찾기() {
    Employee employee = new Employee();
    employee.setName("Jane Doe");
    employee.setEmail("jane.doe@example.com");
    employee.setCode("EMP67890");
    employee.setStatus(Status.ACTIVE);
    employee.setDepartment(department);
    employee.setPosition("Analyst");
    employee.setJoinedAt(Instant.now());

    employeeRepository.save(employee);

    Optional<Employee> foundEmployee = employeeRepository.findByEmail("jane.doe@example.com");
    assertThat(foundEmployee).isPresent();
    assertThat(foundEmployee.get().getName()).isEqualTo("Jane Doe");
  }

  @Test
  void 직원_삭제() {
    Employee employee = new Employee();
    employee.setName("Alice Brown");
    employee.setEmail("alice.brown@example.com");
    employee.setCode("EMP99999");
    employee.setStatus(Status.ACTIVE);
    employee.setDepartment(department);
    employee.setPosition("Consultant");
    employee.setJoinedAt(Instant.now());

    Employee savedEmployee = employeeRepository.save(employee);
    Long employeeId = savedEmployee.getId();

    employeeRepository.deleteById(employeeId);

    Optional<Employee> deletedEmployee = employeeRepository.findById(employeeId);
    assertThat(deletedEmployee).isEmpty();
  }
}
