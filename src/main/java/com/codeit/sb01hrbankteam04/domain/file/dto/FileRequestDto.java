package com.codeit.sb01hrbankteam04.domain.file.dto;

import lombok.Builder;

/**
 * 파일 요청 DTO
 *
 * @param originalFilename 원본 파일명
 * @param contentType      파일 타입
 * @param bytes            파일 byte
 */
@Builder
public record FileRequestDto(String originalFilename, String contentType, byte[] bytes) {

}