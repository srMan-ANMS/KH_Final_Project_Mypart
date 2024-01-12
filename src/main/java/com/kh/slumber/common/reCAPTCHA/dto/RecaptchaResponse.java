package com.kh.slumber.common.reCAPTCHA.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 리캡차 검증 url의 응답 DTO
 * 
 * @success: true|false,      // 이 요청이 사이트에 대한 유효한 reCAPTCHA 토큰인지 여부
 * @challenge_ts: timestamp,  // 챌린지 로드에 대한 timestamp (ISO format yyyy-MM-dd'T'HH:mm:ssZZ)  (유저가 테스트 한 시간)
 * @hostname: string,         // reCAPTCHA가 해결된 사이트의 호스트 이름. 우리 사이트 이름이 들어감.
 * @error-codes: [...]        // 옵션입니다. 받을수도 있고 안 받을 수도 있습니다.
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecaptchaResponse {

  private boolean success;
  private Timestamp challenge_ts;
  private String hostname;
  private String[] error_codes;
}