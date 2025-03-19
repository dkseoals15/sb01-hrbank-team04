package com.codeit.sb01hrbankteam04.domain.employee.repository;

import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  boolean existsByEmail(String email);

  @Query("""
        SELECT e FROM Employee e
        WHERE (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%)
        AND (:employeeNumber IS NULL OR e.code = :employeeNumber)
        AND (:departmentName IS NULL OR e.department.name = :departmentName)
        AND (:position IS NULL OR e.position = :position)
        AND (:hireDateFrom IS NULL OR TO_CHAR(e.joinedAt, 'YYYY-MM-DD') >= :hireDateFrom)
        AND (:hireDateTo IS NULL OR TO_CHAR(e.joinedAt, 'YYYY-MM-DD') <= :hireDateTo)
        AND (:status IS NULL OR e.status = :status)
        AND (:nextIdAfter IS NULL OR e.id > :nextIdAfter)
        ORDER BY 
            CASE WHEN :sortBy = 'hireDate' THEN e.joinedAt END ASC,
            CASE WHEN :sortBy = 'name' THEN e.name END ASC
    """)
  List<Employee> findEmployeesByCursor(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("hireDateFrom") String hireDateFrom,
      @Param("hireDateTo") String hireDateTo,
      @Param("status") String status,
      @Param("nextIdAfter") Long nextIdAfter,
      @Param("sortBy") String sortBy,
      Pageable pageable
  );


}