package com.codeit.sb01hrbankteam04.domain.file.mapper;

import com.codeit.sb01hrbankteam04.domain.file.dto.FileDto;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 Mapper (파일 엔티티/DTO 매핑 수행)
 * <p>
 * 엔티티 -> DTO, DTO -> 엔티티를 할 수 있도록 지원
 */
@Component
public class FileMapper {

  /**
   * file 엔티티를 DTO로 변환
   *
   * @param file 변환할 파일명
   * @return FileDto
   */
  public FileDto toDto(File file) {
    return FileDto.builder()
        .id(file.getId())
        .filename(file.getName())
        .fileSize(file.getSize())
        .contentType(file.getContentType())
        .build();
  }

  /**
   * {@link MultipartFile}을 File 엔티티로 변환
   *
   * @param file 변환할 {@link MultipartFile}
   * @return File 엔티티
   */
  public File toEntity(MultipartFile file) {
    return new File(file.getOriginalFilename(), file.getContentType(), file.getSize());
  }
}
