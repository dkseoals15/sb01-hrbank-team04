package com.codeit.sb01hrbankteam04.repository;

import com.codeit.sb01hrbankteam04.model.Employee;
import com.codeit.sb01hrbankteam04.model.Status;
import java.time.Instant;
import java.time.LocalDate;
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

  Optional<Employee> findByEmail(String mail);

  Optional<Employee> findByCode(String emp1001);
}
