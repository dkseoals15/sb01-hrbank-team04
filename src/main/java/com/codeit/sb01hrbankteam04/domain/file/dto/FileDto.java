package com.codeit.sb01hrbankteam04.domain.file.dto;

import lombok.Builder;

/**
 * File 응답 시 전달 하는 DTO.
 *
 * @param id          파일 아이디
 * @param filename    파일 명
 * @param fileSize    파일 크기
 * @param contentType 파일 컨텐츠 타입 (mime type)
 * @param bytes       바이트 변환 시 데이터
 */
@Builder
public record FileDto(Long id, String filename, Long fileSize, String contentType, byte[] bytes) {

}
