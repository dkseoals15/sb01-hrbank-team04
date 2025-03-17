package com.codeit.sb01hrbankteam04.domain.employeehistory.dto;

import com.codeit.sb01hrbankteam04.domain.employeehistory.type.ModifyType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeLogDto {

  private Long id;

  private ModifyType type;

  private String employeeNumber;

  private String memo;

  private String ipAddress;

  private Instant at;
}
