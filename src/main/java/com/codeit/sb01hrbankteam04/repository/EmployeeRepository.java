package com.codeit.sb01hrbankteam04.repository;

import com.codeit.sb01hrbankteam04.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
