package com.codeit.sb01hrbankteam04.domain.employeehistory.dto.response;

import com.codeit.sb01hrbankteam04.domain.employee.entity.Employee;
import com.codeit.sb01hrbankteam04.domain.employeehistory.entity.EmployeeHistory;
import com.codeit.sb01hrbankteam04.domain.employeehistory.type.ModifyType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionType;

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

  public static ChangeLogDto convertToDto(Object[] result) {
    Employee auditedEntity = (Employee) result[0];
    RevisionType revisionType = (RevisionType) result[2];
    Number revisionNumber = (Number) result[1];
    EmployeeHistory revisionEntity = (EmployeeHistory) result[3];

    return ChangeLogDto.builder()
        .id(revisionNumber.longValue())
        .type(ModifyType.fromRevisionType(revisionType))
        .employeeNumber(auditedEntity.getCode())
        .memo(revisionEntity.getMemo())
        .ipAddress(revisionEntity.getModifiedBy())
        .at(revisionEntity.getCreatedAt())
        .build();
  }
}
