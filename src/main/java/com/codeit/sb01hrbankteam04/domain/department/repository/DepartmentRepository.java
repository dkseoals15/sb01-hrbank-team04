package com.codeit.sb01hrbankteam04.domain.department.repository;

import com.codeit.sb01hrbankteam04.domain.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);

    List<Department> findByNameContainingOrDescriptionContaining(String name, String description);

}
