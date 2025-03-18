package com.codeit.sb01hrbankteam04.domain.employee.controller;

import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeResponse;
import com.codeit.sb01hrbankteam04.domain.employee.service.EmployeeService;
import com.codeit.sb01hrbankteam04.domain.file.FileDto;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EntityController {

  private final EmployeeService employeeService;

  @PostMapping(consumes = "multipart/form-data")
  public ResponseEntity<EmployeeResponse> createEmployee(
      @RequestPart("employeeCreateRequest")EmployeeCreateRequest employeeCreateRequest,
      @RequestPart(value ="profile", required = false)MultipartFile profile
  ){
    Optional<FileDto> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    //profileRequest 필요 -file
    EmployeeResponse createdEmployee =employeeService.create(employeeCreateRequest,profileRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdEmployee);

  }


  @GetMapping("/{id}")
  public ResponseEntity<EmployeeResponse> find(@PathVariable Long id) {
    EmployeeResponse employee = employeeService.find(id);

    if (employee == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(employee);
  }


  private Optional<FileDto> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        FileDto binaryContentCreateRequest = new FileDto(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
