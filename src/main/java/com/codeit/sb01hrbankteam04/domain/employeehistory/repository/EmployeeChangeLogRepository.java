package com.codeit.sb01hrbankteam04.domain.employeehistory.repository;

import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request.ChangeLogRequest;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request.FilterRequest;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.ChangeLogDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.CursorPageResponseEmployeeDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.DiffDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.entity.EmployeeHistory;
import com.codeit.sb01hrbankteam04.domain.employeehistory.type.ModifyType;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class EmployeeChangeLogRepository {

  private final EntityManager entityManager;

  public CursorPageResponseEmployeeDto findChangeLogs(ChangeLogRequest request) {
    AuditReader auditReader = AuditReaderFactory.get(entityManager);
    AuditQuery query = auditReader.createQuery()
        .forRevisionsOfEntity(Employee.class, false, true) // 엔티티 + 리비전 정보 + 삭제된 엔티티도 포함해서 조회하기 위한 설정
        .setMaxResults(request.pageSize());

    FilterRequest filterRequest = new FilterRequest(request.employeeNumber(), request.type(),
        request.memo(), request.ipAddress(), request.atFrom(), request.atTo());

    applyFilters(query, filterRequest);

    if (request.lastRevisionId() != null) {
      query.add(AuditEntity.revisionNumber().lt(request.lastRevisionId()));
    }

    applySorting(query, request.sortField(), request.sortDirection());

    List<ChangeLogDto> logs = query.getResultList().stream()
        .filter(result -> result instanceof Object[])
        .map(result -> ChangeLogDto.convertToDto((Object[]) result))
        .toList();

    Long totalElements = countChangeLogs(filterRequest);

    Long nextIdAfter = logs.isEmpty() ? null : logs.get(logs.size() - 1).getId();
    String nextCursor = nextIdAfter != null ? Base64.getEncoder().encodeToString(nextIdAfter.toString().getBytes()) : null;

    return CursorPageResponseEmployeeDto.builder()
        .content(logs)
        .nextCursor(nextCursor)
        .nextIdAfter(nextIdAfter)
        .size(request.pageSize())
        .totalElements(totalElements)
        .hasNext(logs.size() == request.pageSize())
        .build();
  }

  // 수정 이력 전체 개수
  public Long countChangeLogs(FilterRequest request) {
    AuditReader auditReader = AuditReaderFactory.get(entityManager);

    AuditQuery query = auditReader.createQuery()
        .forRevisionsOfEntity(Employee.class, false, true)
        .addProjection(AuditEntity.revisionNumber().count());

    applyFilters(query, request);

    return (Long) query.getSingleResult();
  }

  private void applyFilters(AuditQuery query, FilterRequest request) {
    if (StringUtils.hasText(request.employeeNumber())) {
      query.add(AuditEntity.property("code").ilike("%" + request.employeeNumber().toLowerCase() + "%"));
    }

    if (request.type() != null) {
      RevisionType revisionType = ModifyType.toRevisionType(request.type());
      query.add(AuditEntity.property("revtype").eq(revisionType));
    }

    if (StringUtils.hasText(request.memo())) {
      query.add(AuditEntity.revisionProperty("memo").ilike("%" + request.memo().toLowerCase() + "%"));
    }

    if (StringUtils.hasText(request.ipAddress())) {
      query.add(AuditEntity.revisionProperty("modifiedBy").like("%" + request.ipAddress() + "%"));
    }

    if (request.atFrom() != null) {
      query.add(AuditEntity.revisionProperty("createdAt").ge(request.atFrom()));
    }

    if (request.atTo() != null) {
      query.add(AuditEntity.revisionProperty("createdAt").le(request.atTo()));
    }
  }

  // AuditQuery에 동적으로 정렬 조건을 추가 (ip 주소와 at)
  private void applySorting(AuditQuery query, String sortField, String sortDirection) {
    boolean ascending = "asc".equalsIgnoreCase(sortDirection);

    Map<String, PropertyNameGetter> sortFieldMappings = Map.of(
        "ipAddress", AuditEntity::property,
        "at", AuditEntity::revisionProperty
    );

    PropertyNameGetter propertyNameGetter = sortFieldMappings.getOrDefault(sortField,
        AuditEntity::revisionProperty);
    query.addOrder(ascending ? propertyNameGetter.get(sortField).asc()
        : propertyNameGetter.get(sortField).desc());
  }

  @FunctionalInterface
  private interface PropertyNameGetter {

    AuditProperty get(String fieldName);

  }

  public List<DiffDto> getRevisionDetails(Long revisionId) {
    AuditReader auditReader = AuditReaderFactory.get(entityManager);

    // 🔥 1. 현재 revisionId에서 employeeId 가져오기
    Object[] result = (Object[]) auditReader.createQuery()
        .forRevisionsOfEntity(Employee.class, false, true)
        .add(AuditEntity.revisionNumber().eq(revisionId))
        .addProjection(AuditEntity.id())
        .addProjection(AuditEntity.revisionType())
        .getSingleResult();

    if (result == null || result.length < 2) {
      return List.of();
    }

    Long employeeId = ((Number) result[0]).longValue();
    RevisionType revtypeEnum = (RevisionType) result[1];
    int revtype = revtypeEnum.getRepresentation();

    System.out.println("📌 조회된 employeeId: " + employeeId + " (revisionId: " + revisionId + ", revtype: " + revtype + ")");

    Employee current;

    if (revtype == 2) {  // 🔥 삭제된 Employee 조회
      List<Object[]> deletedEmployees = auditReader.createQuery()
          .forRevisionsOfEntity(Employee.class, false, true)
          .add(AuditEntity.id().eq(employeeId))
          .add(AuditEntity.revisionNumber().eq(revisionId))
          .getResultList();

      if (deletedEmployees.isEmpty()) {
        System.out.println("📌 ERROR: 삭제된 revisionId(" + revisionId + ")에서 Employee를 찾을 수 없음 (employeeId=" + employeeId + ")");
        return List.of();
      }

      // ✅ Object[] 배열에서 Employee 객체 추출
      current = (Employee) deletedEmployees.get(0)[0];

    } else {
      current = auditReader.find(Employee.class, employeeId, revisionId);
    }

    if (current == null) {
      System.out.println("📌 ERROR: 해당 revisionId(" + revisionId + ")에서 Employee를 찾을 수 없음 (employeeId=" + employeeId + ")");
      return List.of();
    }

    if (revtype == 1) {  // 수정(UPDATE)인 경우
      Number previousRevisionId = (Number) auditReader.createQuery()
          .forRevisionsOfEntity(Employee.class, false, true)
          .add(AuditEntity.id().eq(employeeId))
          .add(AuditEntity.revisionNumber().lt(revisionId))
          .addProjection(AuditEntity.revisionNumber().max())
          .getSingleResult();

      if (previousRevisionId != null) {
        Employee previous = auditReader.find(Employee.class, employeeId, previousRevisionId.longValue());

        if (previous == null) {
          System.out.println("📌 ERROR: 이전 revisionId(" + previousRevisionId + ")에서 Employee를 찾을 수 없음 (employeeId=" + employeeId + ")");
          return List.of();
        }

        return compareChanges(previous, current);
      }
    }

    return mapToDiffDto(current, revtype);
  }



  private List<DiffDto> compareChanges(Employee previous, Employee current) {
    List<DiffDto> changes = new ArrayList<>();

    if (!Objects.equals(previous.getName(), current.getName())) {
      changes.add(new DiffDto("name", previous.getName(), current.getName()));
    }
    if (!Objects.equals(previous.getCode(), current.getCode())) {
      changes.add(new DiffDto("code", previous.getCode(), current.getCode()));
    }
    if (!Objects.equals(previous.getEmail(), current.getEmail())) {
      changes.add(new DiffDto("email", previous.getEmail(), current.getEmail()));
    }
    if (!Objects.equals(previous.getJoinedAt(), current.getJoinedAt())) {
      changes.add(new DiffDto("joinAt", previous.getJoinedAt().toString(), current.getJoinedAt().toString()));
    }
    if (!Objects.equals(previous.getPosition(), current.getPosition())) {
      changes.add(new DiffDto("position", previous.getPosition(), current.getPosition()));
    }
    if (previous.getDepartment() == null && current.getDepartment() == null) {
      // 둘 다 null이면 변경되지 않은 것 -> 출력 안 함
    } else if (previous.getDepartment() == null || current.getDepartment() == null) {
      // 하나만 null이면 변경된 것 -> 출력
      changes.add(new DiffDto("department",
          previous.getDepartment() != null ? previous.getDepartment().getName() : "N/A",
          current.getDepartment() != null ? current.getDepartment().getName() : "N/A"));
    } else if (!Objects.equals(previous.getDepartment().getId(), current.getDepartment().getId())) {
      // ✅ Department ID가 다를 때만 변경된 것으로 판단
      changes.add(new DiffDto("department",
          previous.getDepartment().getName(),
          current.getDepartment().getName()));
    }
    if (!Objects.equals(previous.getStatus(), current.getStatus())) {
      changes.add(new DiffDto("status", previous.getStatus().toString(), current.getStatus().toString()));
    }

    return changes;
  }







  private List<DiffDto> mapToDiffDto(Employee employee, int revtype) {
    List<DiffDto> diffs = new ArrayList<>();

    boolean isDelete = (revtype == 2);  // 삭제(DELETE)면 true

    diffs.add(new DiffDto("name", isDelete ? employee.getName() : null, isDelete ? null : employee.getName()));
    diffs.add(new DiffDto("code", isDelete ? employee.getCode() : null, isDelete ? null : employee.getCode()));
    diffs.add(new DiffDto("email", isDelete ? employee.getEmail() : null, isDelete ? null : employee.getEmail()));
    diffs.add(new DiffDto("joinAt", isDelete ? employee.getJoinedAt().toString() : null, isDelete ? null : employee.getJoinedAt().toString()));
    diffs.add(new DiffDto("position", isDelete ? employee.getPosition() : null, isDelete ? null : employee.getPosition()));
    diffs.add(new DiffDto("department", isDelete ? employee.getDepartment().getName() : null, isDelete ? null : employee.getDepartment().getName()));
    diffs.add(new DiffDto("status", isDelete ? employee.getStatus().toString() : null, isDelete ? null : employee.getStatus().toString()));

    return diffs;
  }

}