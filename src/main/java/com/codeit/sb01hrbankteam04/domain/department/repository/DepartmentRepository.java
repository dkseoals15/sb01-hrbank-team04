package com.codeit.sb01hrbankteam04.domain.department.repository;

import com.codeit.sb01hrbankteam04.domain.department.entity.Department;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 부서(Department) 엔티티를 위한 JPA Repository - 부서 정보를 데이터베이스에서 조회 및 관리하는 역할
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

  /**
   * 부서명을 기준으로 단일 부서 조회 - 부서명이 고유하므로 Optional<Department>으로 반환
   *
   * @param name 검색할 부서명
   * @return 부서 정보 (Optional)
   */
  Optional<Department> findByName(String name);

  /**
   * 부서명 또는 부서 설명에 특정 문자열이 포함된 부서 목록 조회 - 부분 검색(`LIKE` 연산) 적용
   *
   * @param name        검색할 부서명
   * @param description 검색할 부서 설명
   * @return 검색된 부서 목록
   */
  List<Department> findByNameContainingOrDescriptionContaining(String name, String description);


  @Query("""
    SELECT d FROM Department d
    WHERE (:nameOrDescription IS NULL 
        OR d.name LIKE %:nameOrDescription% 
        OR d.description LIKE %:nameOrDescription%)
    AND (:idAfter IS NULL OR d.id > :idAfter)
""")
  List<Department> findDepartmentsByCursor(
          @Param("nameOrDescription") String nameOrDescription,
          @Param("idAfter") Long idAfter,
          Pageable pageable
  );

}
