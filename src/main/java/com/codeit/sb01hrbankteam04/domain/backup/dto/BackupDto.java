package com.codeit.sb01hrbankteam04.domain.backup.dto;

import com.codeit.sb01hrbankteam04.domain.backup.entity.BackupStatus;
import java.time.Instant;
import lombok.Builder;

@Builder
public record BackupDto(Long id, String worker, Instant startedAt, Instant endedAt,
                        BackupStatus status,
                        Long fileId) {

}
