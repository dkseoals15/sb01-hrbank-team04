package com.codeit.sb01hrbankteam04.domain.employee.controller;

import com.codeit.sb01hrbankteam04.domain.employee.controller.api.EmployeeApi;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeDto;
import com.codeit.sb01hrbankteam04.domain.employee.service.EmployeeService;
import com.codeit.sb01hrbankteam04.domain.file.FileCreateRequest;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.ssl.SslProperties.Bundles.Watch.File;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EntityController implements EmployeeApi {

  private final EmployeeService employeeService;

  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  @Override
  public ResponseEntity<EmployeeDto> createEmployee(
      @RequestPart("employeeCreateRequest")EmployeeCreateRequest employeeCreateRequest,
      @RequestPart(value ="profile", required = false)MultipartFile profile
  ){
    Optional<FileCreateRequest> profileRequest = Optional.ofNullable(profile)
        .flatMap(this::resolveProfileRequest);
    //profileRequest 필요 -file
    EmployeeDto createdEmployee =employeeService.create(employeeCreateRequest,profileRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdEmployee);

  }

  private Optional<FileCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        FileCreateRequest binaryContentCreateRequest = new FileCreateRequest(
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
