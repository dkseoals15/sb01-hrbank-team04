package com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request;

import com.codeit.sb01hrbankteam04.domain.employeehistory.type.ModifyType;
import java.time.Instant;

public record ChangeLogRequest(
    Long lastRevisionId,
    int pageSize,
    String employeeNumber,
    ModifyType type,
    String memo,
    String cursor,
    String ipAddress,
    Instant atFrom,
    Instant atTo,
    String sortField,
    String sortDirection
) {

}
