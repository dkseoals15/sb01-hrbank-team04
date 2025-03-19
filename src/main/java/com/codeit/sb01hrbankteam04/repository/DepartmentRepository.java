package com.codeit.sb01hrbankteam04.repository;

import com.codeit.sb01hrbankteam04.domain.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
