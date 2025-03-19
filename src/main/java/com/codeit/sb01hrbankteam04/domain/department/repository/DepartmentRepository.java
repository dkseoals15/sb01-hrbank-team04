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
        WHERE (:name IS NULL OR d.name LIKE %:name%)
        AND (:description IS NULL OR d.description LIKE %:description%)
        AND (:nextIdAfter IS NULL OR d.id > :nextIdAfter)
        ORDER BY 
            CASE WHEN :sortBy = 'name' THEN d.name END ASC,
            CASE WHEN :sortBy = 'establishedDate' THEN d.establishedDate END ASC
    """)
    List<Department> findDepartmentsByCursor(
            @Param("name") String name,
            @Param("description") String description,
            @Param("nextIdAfter") Long nextIdAfter,
            @Param("sortBy") String sortBy,
            Pageable pageable
    );
}
