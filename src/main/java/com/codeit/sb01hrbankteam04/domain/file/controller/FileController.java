package com.codeit.sb01hrbankteam04.domain.file.controller;

import com.codeit.sb01hrbankteam04.domain.file.docs.FileControllerDocs;
import com.codeit.sb01hrbankteam04.domain.file.entity.FileType;
import com.codeit.sb01hrbankteam04.domain.file.service.FileService;
import com.codeit.sb01hrbankteam04.global.response.CustomApiResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 관련 요청을 처리하는 컨트롤러.
 * <p>
 * 파일 업로드, 다운로드 등의 기능을 제공.
 */
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController implements FileControllerDocs {

  private final FileService fileService;

  /**
   * @param id 다운로드 할 파일의 ID
   * @return 파일 다운로드 포맷의 응답 전달
   */
  @GetMapping("/{id}/download")
  public ResponseEntity<?> downloadFile(@PathVariable Long id) {
    return fileService.download(id);
  }

  // TODO: 등록 테스트
  @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public CustomApiResponse<?> saveFile(@RequestBody() MultipartFile file) throws IOException {
    return CustomApiResponse.created(fileService.create(file, FileType.PROFILE));
  }
}
