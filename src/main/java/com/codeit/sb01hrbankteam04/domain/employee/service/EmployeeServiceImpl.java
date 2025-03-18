package com.codeit.sb01hrbankteam04.domain.employee.service;

import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeDto;
import com.codeit.sb01hrbankteam04.domain.employee.repository.EntityRepository;
import com.codeit.sb01hrbankteam04.domain.file.FileCreateRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EntityRepository entityRepository;

  @Override
  public EmployeeDto create(EmployeeCreateRequest employeeCreateRequest,
      Optional<FileCreateRequest> profileRequest) {
    return null;
  }
}
