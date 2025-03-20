package com.codeit.sb01hrbankteam04.domain.department.repository;

import com.codeit.sb01hrbankteam04.domain.department.entity.Department;
import org.springframework.data.domain.Pageable; // ✅ 수정된 import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);

    List<Department> findByNameContainingOrDescriptionContaining(String name, String description);

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
