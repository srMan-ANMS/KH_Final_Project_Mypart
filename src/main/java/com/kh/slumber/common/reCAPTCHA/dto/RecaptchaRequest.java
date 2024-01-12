package com.kh.slumber.common.reCAPTCHA.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * reCAPTCHA 요청을 담을 DTO
 */
@ToString
@Getter
@Setter
public class RecaptchaRequest {
  private String response;

}
