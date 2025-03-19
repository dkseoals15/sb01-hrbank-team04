package com.codeit.sb01hrbankteam04.domain.backup.service;

import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.BackupRequestDto;
import com.codeit.sb01hrbankteam04.domain.backup.dto.CursorPageResponseBackupDto;
import com.codeit.sb01hrbankteam04.domain.backup.entity.Backup;
import com.codeit.sb01hrbankteam04.domain.backup.entity.BackupStatus;
import com.codeit.sb01hrbankteam04.domain.backup.mapper.BackupMapper;
import com.codeit.sb01hrbankteam04.domain.backup.mapper.PageResponseMapper;
import com.codeit.sb01hrbankteam04.domain.backup.repository.BackupRepository;
import com.codeit.sb01hrbankteam04.domain.backup.repository.BackupSpecification;
import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employee.repository.EmployeeRepository;
import com.codeit.sb01hrbankteam04.domain.file.entity.File;
import com.codeit.sb01hrbankteam04.domain.file.entity.FileType;
import com.codeit.sb01hrbankteam04.domain.file.mapper.FileMapper;
import com.codeit.sb01hrbankteam04.domain.file.repository.FileRepository;
import com.codeit.sb01hrbankteam04.domain.storage.FileStorage;
import com.codeit.sb01hrbankteam04.global.util.ip.IPUtils;
import com.opencsv.CSVWriter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

  private final BackupRepository backupRepository;
  private final EmployeeRepository employeeRepository;

  private final BackupMapper backupMapper;
  private final FileMapper fileMapper;

  private final FileRepository fileRepository;
  private final FileStorage fileStorage;
  private final PageResponseMapper<BackupDto> pageResponseMapper;

  @Transactional
  @Override
  public BackupDto create(HttpServletRequest httpServletRequest) {
    // 데이터 백업 이력 등록
    String workerIpAddr = IPUtils.getClientIp(httpServletRequest);
    return performBackup(workerIpAddr);
  }

  @Transactional
  @Override
  public BackupDto create(String worker) {
    // 데이터 백업 이력 등록
    return performBackup(worker);
  }

  @Override
  public CursorPageResponseBackupDto<BackupDto> getBackupListWithCursor(
      BackupRequestDto requestDto) {
    Sort.Direction direction =
        requestDto.sortDirection().equalsIgnoreCase("desc") ? Direction.DESC : Direction.ASC;

    String sortField =
        (requestDto.sortField() == null || requestDto.sortField().isEmpty()) ? "startedAt"
            : requestDto.sortField();

    Pageable pageable = PageRequest.of(0, requestDto.size(), Sort.by(direction, sortField));

    Specification<Backup> spec = BackupSpecification.withFilters(requestDto);
    Page<Backup> backups = backupRepository.findAll(spec, pageable);

    String nextCursor =
        backups.hasNext() ? backups.getContent().get(backups.getContent().size() - 1).getStartedAt()
            .toString() : null;
    Long nextIdAfter =
        backups.hasNext() ? backups.getContent().get(backups.getContent().size() - 1).getId()
            : null;

    return pageResponseMapper.fromPage(backups.map(backupMapper::toDto), nextCursor, nextIdAfter);
  }

  @Override
  public BackupDto getLatestBackup(String status) {
    Backup latestBackup = backupRepository.findTop1ByBackupStatusOrderByEndedAtDesc(
        BackupStatus.fromString(status));
    return backupMapper.toDto(latestBackup);
  }

  private BackupDto performBackup(String worker) {
    Backup backup = backupMapper.toEntity(worker, null);
    backupRepository.save(backup);

    // 데이터 백업 필요 여부 판단
    if (!isBackupNeeded()) {
      // 변경된 데이터가 없다면?
      backup.updateStatus(BackupStatus.SKIPPED);
      backup.updateEndedAt(Instant.now());
      return backupMapper.toDto(backup);
    }

    // 데이터 백업 작업 수행
    try {
      java.io.File backupFile = makeBackupFile(backup.getId());
      File savedBackupFile = fileRepository.save(fileMapper.toEntity(backupFile));

      backup.updateBackupFile(savedBackupFile);
      backup.updateStatus(BackupStatus.COMPLETED);
      backup.updateEndedAt(Instant.now());

      backupRepository.save(backup);
    } catch (IOException e) {
      // 백업 실패 시, 에러 로그 저장
      File errLogFile = makeErrorLog(backup.getId(), e.getMessage());
      fileRepository.save(errLogFile);

      backup.updateBackupFile(errLogFile);
      backup.updateStatus(BackupStatus.FAILED);
      backup.updateEndedAt(Instant.now());
    }
    backupRepository.save(backup);

    return backupMapper.toDto(backupRepository.findById(backup.getId()).orElseThrow());

  }

  /**
   * 백업 필요성 여부 확인
   */
  private boolean isBackupNeeded() {
    // 가장 최근 완료된 배치 시간 조회
    Instant lastCompletedTime = backupRepository.findLastCompletedBatchTime();

    if (lastCompletedTime == null) {
      return true;
    }
    return employeeRepository.existsUpdatedEmployeesAfter(lastCompletedTime);
  }

  /**
   * 백업 파일 생성
   *
   * @param backupId 백업 객체 아이디
   * @return java.io.File 객체 (File 엔티티 아님!)
   * @throws IOException 파일 저장을 진행하기에 {@link IOException} 발생 가능성 존재
   */
  private java.io.File makeBackupFile(Long backupId) throws IOException {
    List<Employee> employees = employeeRepository.getAllForBackUp();
    String filename =
        String.format(FileType.EMPLOYEE_BACKUP.getNameFormat(), backupId, Instant.now().getNano())
            + FileType.EMPLOYEE_BACKUP.getDefaultExtension();
    java.io.File backupFile = new java.io.File(
        System.getProperty("user.dir") + "/temp/" + filename);

    // TODO: 생성 시 OOM 이슈 발생 할 수 있음 -> 해당 부분 고민해보기
    try (CSVWriter writer = new CSVWriter(new FileWriter(backupFile))) {
      writer.writeNext(new String[]{"ID", "직원번호", "이름", "이메일", "부서", "직급", "입사일", "상태"});
      for (Employee employee : employees) {
        writer.writeNext(new String[]{
            String.valueOf(employee.getId()),
            employee.getCode(),
            employee.getName(),
            employee.getEmail(),
            employee.getDepartment().getName(),
            employee.getPosition(),
            employee.getJoinedAt().toString(),
            employee.getStatus().toString()
        });
      }
    }
    return backupFile;
  }

  /**
   * 에러 로그 파일 생성
   *
   * @param backupId     백업 객체 아이디
   * @param errorMessage 에러 메시지
   * @return File 객체
   */
  private File makeErrorLog(Long backupId, String errorMessage) {
    Logger logger = Logger.getLogger(String.valueOf(BackupService.class));

    // Logger의 출력 내용을 StringBuilder로 변환
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream, true, StandardCharsets.UTF_8);

    printStream.println("Backup ID: " + backupId);
    printStream.println(Instant.now());
    printStream.println(errorMessage);

    String filename =
        String.format(FileType.ERROR_LOG.getNameFormat(), backupId, Instant.now().getNano())
            + FileType.ERROR_LOG.getDefaultExtension();

    File f = new File(filename, "text/plain", (long) log.toString().length());
    File saveLog = fileStorage.put(f, outputStream.toByteArray());

    //  ️LocalFileStorage를 사용하여 로그 파일 저장
    return saveLog;
  }
}
