package com.codeit.sb01hrbankteam04.repository;

import com.codeit.sb01hrbankteam04.dto.employee.EmployeeDistributionResponse;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeGroupResult;
import com.codeit.sb01hrbankteam04.model.Employee;
import com.codeit.sb01hrbankteam04.model.Status;
import java.time.Instant;
import java.time.LocalDate;
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
      @Param("status") Status status,
      @Param("fromDate") Instant fromDate,
      @Param("toDate") Instant toDate
  );

  @Query("SELECT new com.codeit.sb01hrbankteam04.dto.employee.EmployeeGroupResult(e.department.name, COUNT(e)) " +
      "FROM Employee e WHERE e.status = :status GROUP BY e.department.name")
  List<EmployeeGroupResult> findEmployeeCountByDepartment(@Param("status") Status status);

  @Query("SELECT new com.codeit.sb01hrbankteam04.dto.employee.EmployeeGroupResult(e.position, COUNT(e)) " +
      "FROM Employee e WHERE e.status = :status GROUP BY e.position")
  List<EmployeeGroupResult> findEmployeeCountByJob(@Param("status") Status status);

  long countByStatus(Status status);


  Optional<Employee> findByEmail(String mail);

  Optional<Employee> findByCode(String emp1001);
}
