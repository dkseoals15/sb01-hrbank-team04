package com.codeit.sb01hrbankteam04.domain.employee.service;

import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeePageRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeePageResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeUpdateRequest;
import com.codeit.sb01hrbankteam04.domain.file.File;
import com.codeit.sb01hrbankteam04.domain.file.FileDto;
import java.util.List;
import java.util.Optional;
import javax.management.InstanceAlreadyExistsException;
import org.springframework.data.domain.Page;

public interface EmployeeService {

  EmployeeResponse create(EmployeeCreateRequest employeeCreateRequest, Optional<FileDto> nullableProfile);

  EmployeeResponse find(Long id);

  EmployeePageResponse getEmployees(
      String nameOrEmail, String employeeNumber, String departmentName, String position,
      String hireDateFrom, String hireDateTo, String status,
      Long nextIdAfter, String cursor, String sortBy, int size);

  EmployeeResponse update(Long id, EmployeeUpdateRequest employeeUpdateRequest,
      Optional<FileDto> proile) throws InstanceAlreadyExistsException;

  void delete(Long id);

}
