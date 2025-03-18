package com.codeit.sb01hrbankteam04.domain.employee.service;

import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeDto;
import com.codeit.sb01hrbankteam04.domain.file.FileCreateRequest;
import java.util.Optional;

public interface EmployeeService {

  public EmployeeDto create(EmployeeCreateRequest employeeCreateRequest, Optional<FileCreateRequest> profileRequest);


}
