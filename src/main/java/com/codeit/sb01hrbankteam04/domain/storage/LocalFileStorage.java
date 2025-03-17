package com.codeit.sb01hrbankteam04.domain.storage;

import com.codeit.sb01hrbankteam04.domain.file.dto.FileDto;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import com.codeit.sb01hrbankteam04.global.exception.CustomException;
import com.codeit.sb01hrbankteam04.global.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 로컬 저장소를 이용한 파일 저장소 구현체
 * <p> 이 클래스는 {@link FileStorage} 인터페이스를 구현하며,
 * 파일 저장, 조회, 다운로드 및 삭제 기능을 제공합니다. </p>
 * <p> 해당 구현체는 <code>hrbank.storage.type</code> 프로퍼티가
 * "local"로 설정된 경우 활성화됩니다. </p>
 */
@Component
@ConditionalOnProperty(name = "hrbank.storage.type", havingValue = "local")
public class LocalFileStorage implements FileStorage {

  private final Path root;

  private static final String FILENAME_REPLACE_VALUE = "%20";
  private static final String FILENAME_REPLACE_REGEX = "\\+";
  private static final String CONTENT_DISPOSITION_HEADER = "attachment; filename*=UTF-8''";

  public LocalFileStorage(@Value("${hrbank.storage.local.root-path}") String rootPath) {
    this.root = Paths.get(System.getProperty("user.dir")).resolve(rootPath);
  }

  @PostConstruct
  private void init() {
    try {
      if (!Files.exists(root)) {
        Files.createDirectories(root);
      }
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_STORAGE_INIT_ERROR);
    }
  }

  private Path resolvePath(String savedFilename) {
    return root.resolve(savedFilename);
  }

  @Override
  public void put(Long fileId, File file, byte[] bytes) {
    try {
      Path filePath = resolvePath(file.getName());
      Files.write(filePath, bytes, StandardOpenOption.CREATE_NEW);
    } catch (Exception e) {
      throw new CustomException(ErrorCode.FILE_WRITE_ERROR);
    }
  }

  @Override
  public ResponseEntity<?> download(FileDto fileDto) {
    Path filePath = resolvePath(fileDto.filename());
    try {
      if (!Files.exists(filePath)) {
        throw new CustomException(ErrorCode.FILE_NOT_FOUND);
      }

      // 파일명 UTF-8 인코딩 (깨짐 방지)
      String encodedFileName = URLEncoder.encode(fileDto.filename(), StandardCharsets.UTF_8)
          .replaceAll(FILENAME_REPLACE_REGEX, FILENAME_REPLACE_VALUE); // 공백을 `%20`으로 변환

      byte[] fileData = Files.readAllBytes(filePath);
      ByteArrayResource resource = new ByteArrayResource(fileData);

      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .header(HttpHeaders.CONTENT_DISPOSITION, CONTENT_DISPOSITION_HEADER + encodedFileName)
          .body(resource);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_DOWNLOAD_ERROR);
    }
  }

  @Override
  public void delete(File file) {
    String fileName = file.getName();
    Path filePath = resolvePath(fileName);
    try {
      if (!Files.exists(filePath)) {
        throw new CustomException(ErrorCode.FILE_NOT_FOUND);
      }
      Files.delete(filePath);
    } catch (NoSuchFileException e) {
      throw new CustomException(ErrorCode.FILE_NOT_FOUND);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
    }
  }
}
