package com.codeit.sb01hrbankteam04.domain.backup.repository;

import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupRequestDto;
import com.codeit.sb01hrbankteam04.domain.backup.entity.Backup;
import com.codeit.sb01hrbankteam04.domain.backup.entity.BackupStatus;
import jakarta.persistence.criteria.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

/**
 * 백업 목록 조회를 위한 필터링 조건을 정의하는 Specification 클래스.
 * <p>
 * JPA Criteria API를 사용하여 동적으로 쿼리를 생성
 */
public class BackupSpecification {

  /**
   * 백얼 리스트 조회 시 필터링을 위함.
   * <p>
   * specification을 생성
   *
   * @param requestDto
   */
  public static Specification<Backup> withFilters(BackupRequestDto requestDto) {
    return ((root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (requestDto.worker() != null && !requestDto.worker().isEmpty()) {
        predicates.add(criteriaBuilder.equal(root.get("backupBy"), requestDto.worker()));
      }
      if (requestDto.status() != null && !requestDto.status().isEmpty()) {
        predicates.add(criteriaBuilder.equal(root.get("backupStatus"),
            BackupStatus.fromString(requestDto.status())));
      }
      if (requestDto.startedAtFrom() != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startedAt"),
            requestDto.startedAtFrom()));
      }
      if (requestDto.startedAtTo() != null) {
        predicates.add(
            criteriaBuilder.lessThanOrEqualTo(root.get("startedAt"), requestDto.startedAtTo()));
      }
      if (requestDto.idAfter() != null) {
        predicates.add(criteriaBuilder.greaterThan(root.get("id"), requestDto.idAfter()));
      }
      if (requestDto.cursor() != null) {
        Instant cursorInstant;
        try {
          cursorInstant = Instant.parse(requestDto.cursor());  // String → Instant 변환
          predicates.add(criteriaBuilder.lessThan(root.get("startedAt"), cursorInstant));
        } catch (Exception e) {
          System.err.println("Invalid cursor format: " + requestDto.cursor());
        }
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    });
  }

}
