package com.codeit.sb01hrbankteam04.domain.backup.dto;

import java.util.List;
import lombok.Builder;

/**
 * Cursor 요소 포함하여 페이지네이션 응답 전달 DTO
 *
 * @param content       전달하고자 하는 리스트
 * @param nextCursor    다음 커서(시간)
 * @param nextIdAfter   다음 아이디
 * @param size          크기
 * @param totalElements 전체 요소들
 * @param hasNext       다음이 존재하는지 여부
 */
@Builder
public record CursorPageResponseBackupDto<T>(List<T> content, String nextCursor,
                                             Long nextIdAfter, int size, Long totalElements,
                                             boolean hasNext) {

}
