package com.codeit.sb01hrbankteam04.domain.backup.dto;

import java.time.Instant;
import lombok.Builder;

/**
 * 백업 조회 시 요청 데이터를 받는 DTO
 *
 * @param worker        작업자 IP
 * @param status        백업 상태 (완료, 실패, 진행 중)
 * @param startedAtFrom 시작 시간 범위 (~부터)
 * @param startedAtTo   시작 시간 범위 (~까지)
 * @param idAfter       다음 ID
 * @param cursor        다음 커서 (시작 시간)
 * @param size          데이터 크기
 * @param sortField     정렬 기준 (어떤 것을 기준으로)
 * @param sortDirection 정렬 기준 (desc / asc)
 */
@Builder
public record BackupRequestDto(String worker, String status, Instant startedAtFrom,
                               Instant startedAtTo,
                               Long idAfter, String cursor, int size, String sortField,
                               String sortDirection) {

}
