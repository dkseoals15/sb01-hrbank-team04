package com.codeit.sb01hrbankteam04.domain.employeehistory.dto;

public record FieldChangeResponse(
    String propertyName,
    String before,
    String after
) {

}
