package com.codeit.sb01hrbankteam04.domain.employee.repository;

import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import com.codeit.sb01hrbankteam04.dto.employee.EmployeeGroupResult;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
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

  /**
   * 마지막 배치가 완료된 시간을 기준으로 그 이후에 업데이트 된 파일이 존재하는지 확인
   *
   * @param lastCompletedTime 마지막 배치가 완료된 시간
   * @return 존재 여부 (boolean)
   */
  @Query("SELECT COUNT(e) > 0 FROM Employee e WHERE e.updatedAt > :lastCompletedTime")
  boolean existsUpdatedEmployeesAfter(@Param("lastCompletedTime") Instant lastCompletedTime);

  /**
   * 백업을 위해 모든 Employee 데이터를 가져옴 (with 부서)
   *
   * @return List<Employee>
   */
  @EntityGraph(attributePaths = {"department"})
  @Query("select e from Employee e")
  List<Employee> getAllForBackUp();
}
