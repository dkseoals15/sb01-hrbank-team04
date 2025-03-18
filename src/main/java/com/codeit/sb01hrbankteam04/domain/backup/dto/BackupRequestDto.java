package com.codeit.sb01hrbankteam04.domain.backup.dto;

import java.time.Instant;
import lombok.Builder;

@Builder
public record BackupRequestDto(String worker, String status, Instant startedAtFrom,
                               Instant startedAtTo,
                               Long idAfter, String cursor, int size, String sortField,
                               String sortDirection) {

}
