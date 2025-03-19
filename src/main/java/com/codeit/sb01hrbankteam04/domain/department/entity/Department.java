package com.codeit.sb01hrbankteam04.domain.department.entity;

import com.codeit.sb01hrbankteam04.global.entity.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Entity
@Table(name = "department")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Department extends BaseUpdatableEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(name = "established_at", nullable = false)
    private Instant establishedDate;

    public void updateDepartment(String name, String description, Instant establishedDate) {
        this.name = name;
        this.description = description;
        this.establishedDate = establishedDate;
    }


    //Instant를 UTC 기준으로 "00:00:00"으로 변환하여 날짜만 저장
    private Instant toUtcStartOfDay(Instant instant) {
        if (instant == null) return null;
        return instant.atZone(ZoneOffset.UTC).toLocalDate().atStartOfDay().toInstant(ZoneOffset.UTC);
    }
}
