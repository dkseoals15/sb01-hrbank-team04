package com.codeit.sb01hrbankteam04.service;

import com.codeit.sb01hrbankteam04.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Transactional
public class EmployeeService {
  private final EmployeeRepository employeeRepository;

}
