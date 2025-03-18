package com.codeit.sb01hrbankteam04.domain.employee.service;

import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeUpdateRequest;
import com.codeit.sb01hrbankteam04.domain.file.File;
import com.codeit.sb01hrbankteam04.domain.file.FileDto;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {

  EmployeeResponse create(EmployeeCreateRequest employeeCreateRequest, Optional<FileDto> nullableProfile);

  EmployeeResponse find(Long id);

  List<EmployeeResponse> findAll();

  EmployeeResponse update(Long id, EmployeeUpdateRequest employeeUpdateRequest,
      Optional<FileDto> proile);

  void delete(Long id);

}
