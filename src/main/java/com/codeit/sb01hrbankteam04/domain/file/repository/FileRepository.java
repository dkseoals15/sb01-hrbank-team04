package com.codeit.sb01hrbankteam04.domain.file.repository;

import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 파일 레포지토리 - JPA.
 */
@Repository
public interface FileRepository extends JpaRepository<File, Long> {

  /**
   * 생성 일시만 불러오기
   *
   * @param id 불러오고자 하는 파일의 아이디
   * @return 해당 파일의 생성일시
   */
  @Query("SELECT f.createdAt FROM File f WHERE f.id = :id")
  Optional<Instant> findCreatedAtById(@Param("id") Long id);

}
