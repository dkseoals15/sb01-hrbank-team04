package com.codeit.sb01hrbankteam04.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "employee", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "code")
})
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt = Instant.now();

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 20, nullable = false)
  private Status status;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "email", length = 70, nullable = false, unique = true)
  private String email;

  @Column(name = "code", length = 30, nullable = false, unique = true)
  private String code;

  @ManyToOne
  @JoinColumn(name = "department_id", nullable = false)
  private Department department;

  @Column(name = "position", length = 50)
  private String position;

  @Column(name = "joined_at")
  private Instant joinedAt;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "file_id")
  private File file;
}
