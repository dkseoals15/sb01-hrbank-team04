package com.codeit.sb01hrbankteam04.domain.employee.repository;

import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeGroupResult;
import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employee.entity.EmployeeStatusType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  //직원관리
  boolean existsByEmail(String email);

  /**
   * 직원 전체 조회 메서드 커서 기반 페이지네이션 적용
   *
   * @param nameOrEmail    이름 또는 이메일, 부분 일치 검색
   * @param employeeNumber 사번, 부분 일치 검색
   * @param departmentName 부서명, 직원 부분 일치 검색
   * @param position       직함, 부분 일치 검색
   * @param hireDateFrom   입사일 시작 범위
   * @param hireDateTo     입사일 종료 범위
   * @param status         상태, 완전 일치 검색
   * @param nextIdAfter    커서 기반 페이지네이션을 위한 마지막 조회된 직원 ID 이후의 데이터 조회
   * @param sortBy         정렬 기준 (입사일, 이름, 사번 등)
   * @param sortDirection  정렬 방향
   * @return 직원 목록 (조건에 맞는 직원들)
   */

  @Query("""
          SELECT e FROM Employee e
          WHERE (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%)
          AND (:employeeNumber IS NULL OR e.code = :employeeNumber)
          AND (:departmentName IS NULL OR e.department.name  LIKE %:departmentName% )
          AND (:position IS NULL OR e.position LIKE %:position%)
          AND (:hireDateFrom IS NULL OR TO_CHAR(e.joinedAt, 'YYYY-MM-DD') >= :hireDateFrom)
          AND (:hireDateTo IS NULL OR TO_CHAR(e.joinedAt, 'YYYY-MM-DD') <= :hireDateTo)
          AND (:status IS NULL 
                OR (
                  (:status = 'ACTIVE' AND e.status ='ACTIVE')
                    OR (:status = 'ON_LEAVE' AND e.status ='ON_LEAVE')
                    OR (:status = 'RESIGNED' AND e.status ='RESIGNED')
                )
              )
          AND (:nextIdAfter IS NULL OR e.id > :nextIdAfter)
          ORDER BY 
              CASE WHEN :sortBy = 'hireDate' AND :sortDirection = 'asc' THEN e.joinedAt END ASC,
              CASE WHEN :sortBy = 'hireDate' AND :sortDirection = 'desc' THEN e.joinedAt END DESC,
              CASE WHEN :sortBy = 'name' AND :sortDirection = 'asc' THEN e.name END ASC,
              CASE WHEN :sortBy = 'name' AND :sortDirection = 'desc' THEN e.name END DESC,
              CASE WHEN :sortBy = 'employeeNumber' AND :sortDirection = 'asc' Then e.code ENd ASC,
              CASE WHEN :sortBy = 'employeeNumber' AND :sortDirection = 'desc' Then e.code ENd DESC
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
      @Param("sortDirection") String sortDirection,
      Pageable pageable
  );

  //대시보드

  /**
   * 직원 중 필터링 값(상태, 입사일)과 동일한 수 카운팅
   */
  @Query("SELECT COUNT(e) FROM Employee e " +
      "WHERE (:status IS NULL OR e.status = :status) " +
      "AND ( e.joinedAt >= :fromDate) " +
      "AND ( e.joinedAt <= :toDate)")
  int countEmployees(
      @Param("status") EmployeeStatusType status,
      @Param("fromDate") Instant fromDate,
      @Param("toDate") Instant toDate
  );

  // 위치 변경
  @Query(
      "SELECT new com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeGroupResult(e.department.name, COUNT(e)) "
          +
          "FROM Employee e WHERE e.status = :status GROUP BY e.department.name")
  List<EmployeeGroupResult> findEmployeeCountByDepartment(
      @Param("status") EmployeeStatusType status);

  /**
   * 직원 중 필터링 값(상태, 입사일)과 동일한 수 카운팅
   *
   * @param { status 직원의 상태 값,  fromDate 입사일 계산 시작 날짜, toDate 입사일 계산 끝 날짜 }
   * @return 필터링된 직원의수
   */
  @Query(
      "SELECT new com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeGroupResult(e.position, COUNT(e)) "
          +
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