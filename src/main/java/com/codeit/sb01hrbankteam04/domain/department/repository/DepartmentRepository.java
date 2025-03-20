package com.codeit.sb01hrbankteam04.domain.department.repository;

import com.codeit.sb01hrbankteam04.domain.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 부서(Department) 엔티티를 위한 JPA Repository
 * - 부서 정보를 데이터베이스에서 조회 및 관리하는 역할
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * 부서명을 기준으로 단일 부서 조회
     * - 부서명이 고유하므로 Optional<Department>으로 반환
     * @param name 검색할 부서명
     * @return 부서 정보 (Optional)
     */
    Optional<Department> findByName(String name);

    /**
     * 부서명 또는 부서 설명에 특정 문자열이 포함된 부서 목록 조회
     * - 부분 검색(`LIKE` 연산) 적용
     * @param name 검색할 부서명
     * @param description 검색할 부서 설명
     * @return 검색된 부서 목록
     */
    List<Department> findByNameContainingOrDescriptionContaining(String name, String description);

    /**
     * 커서 기반 페이지네이션을 위한 부서 목록 조회
     * - `nameOrDescription`: 부서명 또는 설명에 특정 단어가 포함된 경우 필터링
     * - `idAfter`: 특정 ID 이후의 데이터만 조회 (커서 기반 페이지네이션)
     * - `sortField`, `sortDirection`: 정렬 기준 및 방향 설정
     * @param nameOrDescription 검색할 키워드 (부서명 또는 설명)
     * @param idAfter 특정 ID 이후의 데이터 조회
     * @param sortField 정렬 기준 (`name`, `establishedDate`)
     * @param sortDirection 정렬 방향 (`asc`, `desc`)
     * @param size 한 번에 조회할 데이터 개수 (페이징)
     * @return 검색된 부서 목록
     */
    @Query("""
        SELECT d FROM Department d
        WHERE (:nameOrDescription IS NULL OR d.name LIKE %:nameOrDescription% OR d.description LIKE %:nameOrDescription%)
        AND (:idAfter IS NULL OR d.id > :idAfter)
        ORDER BY 
            CASE WHEN :sortField = 'name' AND :sortDirection = 'asc' THEN d.name END ASC,
            CASE WHEN :sortField = 'name' AND :sortDirection = 'desc' THEN d.name END DESC,
            CASE WHEN :sortField = 'establishedDate' AND :sortDirection = 'asc' THEN d.establishedDate END ASC,
            CASE WHEN :sortField = 'establishedDate' AND :sortDirection = 'desc' THEN d.establishedDate END DESC
    """)
    List<Department> findDepartmentsByCursor(
            @Param("nameOrDescription") String nameOrDescription,
            @Param("idAfter") Long idAfter,
            @Param("sortField") String sortField,
            @Param("sortDirection") String sortDirection,
            @Param("size") int size);
}
