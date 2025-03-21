package com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CursorPageResponseEmployeeDto {

  private List<ChangeLogDto> content;

  private String nextCursor;

  private Long nextIdAfter;

  private int size;

  private Long totalElements;

  private boolean hasNext;

}
