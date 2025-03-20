package com.codeit.sb01hrbankteam04.domain.department.controller;

import com.codeit.sb01hrbankteam04.domain.department.dto.CursorPageResponseDepartmentDto;
import com.codeit.sb01hrbankteam04.domain.department.dto.DepartmentUpdateRequest;
import com.codeit.sb01hrbankteam04.domain.department.service.DepartmentService;
import com.codeit.sb01hrbankteam04.domain.department.dto.DepartmentCreateRequest;
import com.codeit.sb01hrbankteam04.domain.department.dto.DepartmentDto;
import com.codeit.sb01hrbankteam04.global.response.CustomApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(
            @RequestBody DepartmentCreateRequest request) {
        return ResponseEntity.ok(departmentService.createDepartment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDto> getDepartment(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping
    public CursorPageResponseDepartmentDto getDepartments(
            @RequestParam(required = false) String nameOrDescription,
            @RequestParam(required = false) Long idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "establishedDate") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        return departmentService.getDepartments(nameOrDescription, idAfter, cursor, size, sortField, sortDirection);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(
            @PathVariable Long id,
            @RequestBody DepartmentUpdateRequest request) {

        DepartmentDto updatedDepartment = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

}
