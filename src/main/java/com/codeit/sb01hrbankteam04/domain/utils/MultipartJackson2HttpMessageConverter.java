package com.codeit.sb01hrbankteam04.domain.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Type;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

/**
 * "Content-Type: multipart/form-data" 헤더를 지원하는 HTTP 요청 변환기
 *
 * Swagger의 Content type 'application/octet-stream' not supported 오류를 해결합니다.
 */
@Component
public class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {


  public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
    super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
  }

  @Override
  public boolean canWrite(Class<?> clazz, MediaType mediaType) {
    return false;
  }

  @Override
  public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
    return false;
  }

  @Override
  protected boolean canWrite(MediaType mediaType) {
    return false;
  }
}