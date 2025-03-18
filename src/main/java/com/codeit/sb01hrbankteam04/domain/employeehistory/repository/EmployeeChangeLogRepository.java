package com.codeit.sb01hrbankteam04.domain.employeehistory.repository;

import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request.ChangeLogRequest;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request.FilterRequest;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.ChangeLogDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response.CursorPageResponseEmployeeDto;
import com.codeit.sb01hrbankteam04.domain.employeehistory.type.ModifyType;
import jakarta.persistence.EntityManager;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.AuditProperty;
import org.springframework.stereotype.Repository;
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
      query.add(AuditEntity.property("id").like("%" + request.employeeNumber() + "%"));
    }

    int revisionType = ModifyType.toRevisionType(request.type());
    query.add(AuditEntity.property("revisionType").eq(revisionType));

    if (StringUtils.hasText(request.memo())) {
      query.add(AuditEntity.revisionProperty("memo").like("%" + request.memo() + "%"));
    }

    if (StringUtils.hasText(request.ipAddress())) {
      query.add(AuditEntity.property("ipAddress").like("%" + request.ipAddress() + "%"));
    }

    if (request.atFrom() != null) {
      query.add(AuditEntity.revisionProperty("modifiedBy").ge(request.atFrom()));
    }

    if (request.atTo() != null) {
      query.add(AuditEntity.revisionProperty("modifiedBy").le(request.atTo()));
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
}
