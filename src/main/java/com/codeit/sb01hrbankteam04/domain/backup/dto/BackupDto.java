package com.codeit.sb01hrbankteam04.domain.backup.dto;

import com.codeit.sb01hrbankteam04.domain.backup.entity.BackupStatus;
import java.time.Instant;
import lombok.Builder;

/**
 * Backup 전반적으로 사용되는 DTO (응답 등)
 *
 * @param id        아이디
 * @param worker    요청한 IP (db는 backup_by로 정의되어 있음)
 * @param startedAt 작업 시작 시간
 * @param endedAt   작업을 마친 시간
 * @param status    백업 상태 (완료, 실패, 진행 중)
 * @param fileId    백업 파일 아이디
 */
@Builder
public record BackupDto(Long id, String worker, Instant startedAt, Instant endedAt,
                        BackupStatus status,
                        Long fileId) {

}
