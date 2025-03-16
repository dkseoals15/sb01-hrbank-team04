package com.codeit.sb01hrbankteam04.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "departments", uniqueConstraints = {
    @UniqueConstraint(columnNames = "name")
})
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt = Instant.now();

  @Column(name = "name", length = 100, nullable = false, unique = true)
  private String name;

  @Column(name = "description", length = 200)
  private String description;

  @Column(name = "established_at")
  private Instant establishedAt;

  @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Employee> employees;
}