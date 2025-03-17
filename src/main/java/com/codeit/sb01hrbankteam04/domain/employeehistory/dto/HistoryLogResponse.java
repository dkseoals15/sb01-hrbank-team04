package com.codeit.sb01hrbankteam04.domain.employeehistory.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoryLogResponse {

  private List<EmployeeHistoryDTO> content;

  private String nextCursor;

  private Long nextIdAfter;

  private int size;

  private Long totalElements;

  private boolean hasNext;

}
