package com.codeit.sb01hrbankteam04.repository.employee;

import com.codeit.sb01hrbankteam04.domain.department.Department;
import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.domain.employee.repository.EmployeeRepository;
import com.codeit.sb01hrbankteam04.repository.DepartmentRepository;
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
    Employee employee = new Employee(
        EmployeeStatusType.재직중, // 또는 다른 상태값을 설정 (예: 재직중, 휴직중 등)
        "John Doe",
        "john.doe@example.com",
        "EMP12345",
        department,
        "Manager",
        Instant.now(),
        null // profile은 선택사항으로 null로 설정
    );
    employeeRepository.save(employee);

    Employee savedEmployee = employeeRepository.save(employee);

    assertThat(savedEmployee.getId()).isNotNull();
    assertThat(savedEmployee.getName()).isEqualTo("John Doe");
  }

  @Test
  void 이메일로_직원찾기() {
    Employee employee = new Employee(
        EmployeeStatusType.재직중, // 또는 다른 상태값으로 설정
        "Jane Doe",
        "jane.doe@example.com",
        "EMP67890",
        department,
        "Analyst",
        Instant.now(),
        null // profile (optional, null로 설정)
    );
    employeeRepository.save(employee);

    employeeRepository.save(employee);

    Optional<Employee> foundEmployee = employeeRepository.findByEmail("jane.doe@example.com");
    assertThat(foundEmployee).isPresent();
    assertThat(foundEmployee.get().getName()).isEqualTo("Jane Doe");
  }

  @Test
  void 직원_삭제() {
    Employee employee = new Employee(
        EmployeeStatusType.재직중, // 또는 다른 상태값을 설정 (예: 재직중, 휴직중 등)
        "Alice Brown",
        "alice.brown@example.com",
        "EMP99999",
        department,
        "Consultant",
        Instant.now(),
        null // profile은 선택사항으로 null로 설정
    );
    employeeRepository.save(employee);

    Employee savedEmployee = employeeRepository.save(employee);
    Long employeeId = savedEmployee.getId();

    employeeRepository.deleteById(employeeId);

    Optional<Employee> deletedEmployee = employeeRepository.findById(employeeId);
    assertThat(deletedEmployee).isEmpty();
  }
}
