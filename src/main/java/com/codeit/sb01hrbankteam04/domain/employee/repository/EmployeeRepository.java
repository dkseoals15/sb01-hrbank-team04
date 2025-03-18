package com.codeit.sb01hrbankteam04.domain.employee.repository;

import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
// TODO: EntityRepository -> EmployeeRepository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  @Query("SELECT COUNT(e) > 0 FROM Employee e WHERE e.updatedAt > :lastCompletedTime")
  boolean existsUpdatedEmployeesAfter(@Param("lastCompletedTime") Instant lastCompletedTime);

  @EntityGraph(attributePaths = {"department"})
  @Query("select e from Employee e")
  List<Employee> getAllForBackUp();

}
