package com.codeit.sb01hrbankteam04.domain.employee.controller;

import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeCreateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeePageResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeResponse;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeePageRequest;
import com.codeit.sb01hrbankteam04.domain.employee.dto.EmployeeUpdateRequest;
import com.codeit.sb01hrbankteam04.domain.employee.service.EmployeeService;
import com.codeit.sb01hrbankteam04.domain.file.FileDto;
import java.io.IOException;
import java.util.Optional;
import javax.management.InstanceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EntityController {

  private final EmployeeService employeeService;

  @GetMapping
  public ResponseEntity<EmployeePageResponse> getEmployees(
      @RequestParam(required = false) String nameOrEmail,
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) String departmentName,
      @RequestParam(required = false) String position,
      @RequestParam(required = false) String hireDateFrom,
      @RequestParam(required = false) String hireDateTo,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) Long nextIdAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "hireDate") String sortBy,
      @RequestParam(defaultValue = "10") int size) {

    return ResponseEntity.ok(employeeService.getEmployees(
        nameOrEmail, employeeNumber, departmentName, position, hireDateFrom, hireDateTo, status, nextIdAfter, cursor,sortBy, size));
  }

  //직원 등록
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<EmployeeResponse> createEmployee(
      @RequestPart("employeeCreateRequest")EmployeeCreateRequest employeeCreateRequest,
      @RequestPart(value ="profile", required = false)MultipartFile profile
  ){

    //파일 Dto 변환 처리
    Optional<FileDto> profileRequest = fileToDto(profile);

    EmployeeResponse createdEmployee =employeeService.create(employeeCreateRequest,profileRequest);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdEmployee);

  }


  //직원 상세 조회
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeResponse> find(@PathVariable Long id) {
    EmployeeResponse employee = employeeService.find(id);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(employee);
  }

  
  //직원 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete (@PathVariable ("id")Long id){
    employeeService.delete(id);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
  
  //직원 수정
  @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeResponse> update (@PathVariable ("id") Long id,
      @RequestPart EmployeeUpdateRequest updateRequest,
      @RequestPart MultipartFile profile) throws InstanceAlreadyExistsException {

    //파일 Dto 변환 처리
    Optional<FileDto> profileRequest = fileToDto(profile);

    EmployeeResponse employeeResponse = employeeService.update(id, updateRequest, profileRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(employeeResponse);


  }


  //보일러 플레이트
  private Optional<FileDto> fileToDto(MultipartFile file) {
    Optional<FileDto> profileRequest = Optional.ofNullable(file)
        .flatMap(this::resolveProfileRequest);
    return profileRequest;
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
