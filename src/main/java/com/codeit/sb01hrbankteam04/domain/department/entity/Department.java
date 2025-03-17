package com.codeit.sb01hrbankteam04.domain.department.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "department")
@Getter
@NoArgsConstructor

public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(name = "established_at", nullable = false)
    private LocalDateTime establishedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDepartment(String name, String description, LocalDateTime establishedAt) {
        this.name = name;
        this.description = description;
        this.establishedAt = establishedAt;
        this.updatedAt = LocalDateTime.now(); // 수동으로 updatedAt 갱신
    }


}
