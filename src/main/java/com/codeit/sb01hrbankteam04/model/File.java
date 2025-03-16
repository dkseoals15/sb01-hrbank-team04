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
@Table(name = "files")
public class File{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt = Instant.now();

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "content_type", length = 100, nullable = false)
  private String contentType;

  @Column(name = "size", nullable = false)
  private Long size;
}
