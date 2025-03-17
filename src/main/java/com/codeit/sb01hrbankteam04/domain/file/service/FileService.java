package com.codeit.sb01hrbankteam04.domain.file.service;

import com.codeit.sb01hrbankteam04.domain.file.dto.FileDto;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import com.codeit.sb01hrbankteam04.domain.file.entity.FileType;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  /**
   * 파일 저장 데이터베이스에는 File과 관련된 메타 정보를 저장.
   * <p>
   * application.yml 의 hrbank.storage 설정에 따라 사용할 저장소가 결정된다.
   *
   * @param file     요청 받은 {@link MultipartFile} 파일 데이터
   * @param fileType {@link FileType} Enum, 해당 FileType에 따라 저장 시 파일 명 등이 달라진다.
   * @return FileDto
   * @throws IOException - 파일 저장을 진행하기에 {@link IOException}이 발생할 수 있음
   */
  FileDto create(MultipartFile file, FileType fileType) throws IOException;

  /**
   * fileId를 통해 File을 찾아 DTO로 변환하여 반환한다.
   *
   * @param fileId 찾고자 하는 파일 아이디
   * @return FileDto
   */
  FileDto find(Long fileId);

  /**
   * 저장된 전체 File을 리턴한다.
   *
   * @return List<FileDto>
   */
  List<FileDto> findAll();

  /**
   * File 엔티티를 전달 받아 삭제한다.
   * <p>
   * 삭제 시, 저장소에 저장된 실제 파일 또한 함께 삭제된다.
   *
   * @param file 파일 엔티티
   */
  void delete(File file);

  /**
   * 삭제하고자 하는 파일 아이디를 전달 받아 삭제한다.
   * <p>
   * 삭제 시, 저장소에 저장된 실제 파일 또한 함께 삭제된다.
   *
   * @param fileId 삭제하고자 하는 파일 아이디
   */
  void deleteById(Long fileId);

  /**
   * 사용자가 파일을 다운로드 할 수 있도록 다운로드가 가능한 형식의 응답을 전달
   *
   * @param fileId 다운받고자 하는 파일 아이디
   * @return 다운로드 가능한 형식의 응답
   */
  ResponseEntity<?> download(Long fileId);
}
