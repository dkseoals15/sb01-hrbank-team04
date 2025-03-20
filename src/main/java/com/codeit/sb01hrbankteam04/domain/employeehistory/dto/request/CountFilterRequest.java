package com.codeit.sb01hrbankteam04.domain.employeehistory.dto.request;

import java.time.Instant;

public record CountFilterRequest(
    Instant fromDate,
    Instant toDate
) {

}
