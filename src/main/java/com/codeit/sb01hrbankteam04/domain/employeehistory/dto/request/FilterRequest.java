package com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request;

import com.codeit.sb01hrbankteam04.domain.employeehistory.type.ModifyType;
import java.time.Instant;

// 수정 이력 조회 시 사용되는 필드
public record FilterRequest(
    String employeeNumber,
    ModifyType type,
    String memo,
    String ipAddress,
    Instant atFrom,
    Instant atTo
) {

}