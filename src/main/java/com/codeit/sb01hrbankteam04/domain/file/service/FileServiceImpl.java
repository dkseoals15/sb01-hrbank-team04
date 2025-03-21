package com.codeit.sb01hrbankteam04.domain.file.service;

import com.codeit.sb01hrbankteam04.domain.file.dto.FileDto;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import com.codeit.sb01hrbankteam04.domain.file.entity.FileType;
import com.codeit.sb01hrbankteam04.domain.file.mapper.FileMapper;
import com.codeit.sb01hrbankteam04.domain.file.repository.FileRepository;
import com.codeit.sb01hrbankteam04.domain.storage.FileStorage;
import com.codeit.sb01hrbankteam04.global.exception.CustomException;
import com.codeit.sb01hrbankteam04.global.exception.ErrorCode;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * File 처리 관련 서비스 구현 클래스
 * <p>
 * 파일 저장, 조회, 다운로드 및 삭제 기능 제공
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  private final FileRepository fileRepository;
  private final FileStorage fileStorage;
  private final FileMapper fileMapper;

  @Transactional
  @Override
  public FileDto create(MultipartFile file, FileType fileType) throws IOException {
    System.out.println(file.getOriginalFilename());
    File saveFile = fileRepository.saveAndFlush(fileMapper.toEntity(file));
    setFilename(saveFile, fileType);
    fileStorage.put(saveFile, file.getBytes());
    return fileMapper.toDto(saveFile);
  }

  // 파일 이름 설정
  private void setFilename(File file, FileType fileType) {
    String extension = fileType.getDefaultExtension();
    System.out.println("SetFilename: " + file.getId());
    // 파일명
    Instant createdTime = fileRepository.findCreatedAtById(file.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));
    if (fileType == FileType.PROFILE) {
      extension = extractImageExtension(file.getName(), fileType.getDefaultExtension());
    }
    String filename =
        String.format(fileType.getNameFormat(), file.getId(), createdTime.getNano()) + extension;
    file.updateName(filename);
  }

  // 확장자 추출
  private String extractImageExtension(String originalFilename, String defaultExtension) {
    final String SEPERATE_EXTENSION_VALUE = ".";

    if (originalFilename == null || originalFilename.isEmpty()) {
      return defaultExtension; // 기본 확장자
    }

    int dotIndex = originalFilename.lastIndexOf(SEPERATE_EXTENSION_VALUE);
    if (dotIndex == -1 || dotIndex == originalFilename.length() - 1) {
      return defaultExtension; // 확장자가 없으면 기본값 적용
    }
    return originalFilename.substring(dotIndex);
  }

  @Override
  public FileDto find(Long fileId) {
    return fileMapper.toDto(
        fileRepository.findById(fileId)
            .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND)));
  }

  @Override
  public List<FileDto> findAll() {
    return fileRepository.findAll().stream().map(fileMapper::toDto).collect(Collectors.toList());
  }

  @Override
  public void delete(File file) {
    fileRepository.delete(file);
    fileStorage.delete(file);
  }

  @Override
  public void deleteById(Long fileId) {
    File file = fileRepository.findById(fileId)
        .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));
    fileRepository.delete(file);
    fileStorage.delete(file);
  }

  @Override
  public ResponseEntity<?> download(Long fileId) {
    File file = fileRepository.findById(fileId)
        .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));
    return fileStorage.download(fileMapper.toDto(file));
  }
}
