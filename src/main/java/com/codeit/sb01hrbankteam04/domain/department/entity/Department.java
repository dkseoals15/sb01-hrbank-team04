package com.codeit.sb01hrbankteam04.domain.department.entity;

import com.codeit.sb01hrbankteam04.global.entity.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * 부서(Department) 엔티티 클래스
 * - 부서 정보를 저장하는 테이블과 매핑됨
 * - 기본적으로 ID, 생성/수정 시간(`BaseUpdatableEntity`)을 포함
 */
@Entity
@Table(name = "department")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Department extends BaseUpdatableEntity {

    /**
     * 부서명 (중복 불가, 최대 100자)
     * - 필수 입력 필드 (`nullable = false`)
     * - 고유 값 보장 (`unique = true`)
     */
    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /**
     * 부서 설명 (최대 200자)
     * - 필수 입력 필드 (`nullable = false`)
     */
    @Column(nullable = false, length = 200)
    private String description;

    /**
     * 부서 설립일 (UTC 기준으로 저장)
     * - 필수 입력 필드 (`nullable = false`)
     */
    @Column(name = "established_at", nullable = false)
    private Instant establishedDate;

    /**
     * 부서 정보 업데이트 메서드
     * - 부서명, 설명, 설립일을 수정 가능
     * @param name 새로운 부서명
     * @param description 새로운 부서 설명
     * @param establishedDate 새로운 설립일 (`Instant` 타입)
     */
    public void updateDepartment(String name, String description, Instant establishedDate) {
        this.name = name;
        this.description = description;
        this.establishedDate = establishedDate;
    }

    /**
     * `Instant` 값을 UTC 기준 "00:00:00" 시점으로 변환
     * - 날짜만 저장하고, 시간을 00:00:00으로 설정
     * - 데이터 일관성을 유지하기 위해 사용됨
     * @param instant 변환할 `Instant` 값
     * @return UTC 기준 하루의 시작 (`00:00:00`)
     */
    private Instant toUtcStartOfDay(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(ZoneOffset.UTC).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC);
    }
}
