package com.codeit.sb01hrbankteam04.domain.backup.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record CursorPageResponseBackupDto<T>(List<T> content, String nextCursor,
                                             Long nextIdAfter, int size, Long totalElements,
                                             boolean hasNext) {

}
