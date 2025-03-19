package com.codeit.sb01hrbankteam04.domain.employee.repository;

import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeGroupResult;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  @Query("SELECT COUNT(e) FROM Employee e " +
      "WHERE (:status IS NULL OR e.status = :status) " +
      "AND ( e.joinedAt >= :fromDate) " +
      "AND ( e.joinedAt <= :toDate)")
  int countEmployees(
      @Param("status") EmployeeStatusType status,
      @Param("fromDate") Instant fromDate,
      @Param("toDate") Instant toDate
  );

  @Query("SELECT new com.codeit.sb01hrbankteam04.dto.employee.EmployeeGroupResult(e.department.name, COUNT(e)) " +
      "FROM Employee e WHERE e.status = :status GROUP BY e.department.name")
  List<EmployeeGroupResult> findEmployeeCountByDepartment(@Param("status") EmployeeStatusType status);

  @Query("SELECT new com.codeit.sb01hrbankteam04.dto.employee.EmployeeGroupResult(e.position, COUNT(e)) " +
      "FROM Employee e WHERE e.status = :status GROUP BY e.position")
  List<EmployeeGroupResult> findEmployeeCountByPosition(@Param("status") EmployeeStatusType status);

  long countByStatus(EmployeeStatusType status);

  Optional<Employee> findByEmail(String mail);

  Optional<Employee> findByCode(String emp1001);
}
