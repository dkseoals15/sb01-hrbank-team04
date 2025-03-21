package com.codeit.sb01hrbankteam04.domain.backup.mapper;

import com.codeit.sb01hrbankteam04.domain.backup.dto.CursorPageResponseBackupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

/**
 * 페이징 관련 Mapper 클래스
 */
@Component
public class PageResponseMapper<T> {

  /**
   * Slice 데이터를 변환
   *
   * @param slice
   * @param nextCursor
   * @param nextIdAfter
   */
  public CursorPageResponseBackupDto<T> fromSlice(Slice<T> slice, String nextCursor,
      Long nextIdAfter) {
    return new CursorPageResponseBackupDto<>(
        slice.getContent(),
        nextCursor,
        nextIdAfter,
        slice.getSize(),
        (long) slice.getNumberOfElements(),
        slice.hasNext()
    );
  }

  /**
   * Page 데이터를 변환
   *
   * @param page
   * @param nextCursor
   * @param nextIdAfter
   */
  public CursorPageResponseBackupDto<T> fromPage(Page<T> page, String nextCursor,
      Long nextIdAfter) {
    return new CursorPageResponseBackupDto<>(
        page.getContent(),
        nextCursor,
        nextIdAfter,
        page.getSize(),
        (long) page.getNumberOfElements(),
        page.hasNext()
    );
  }

}
