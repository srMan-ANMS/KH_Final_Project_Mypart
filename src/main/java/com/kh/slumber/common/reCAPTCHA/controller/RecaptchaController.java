package com.kh.slumber.common.reCAPTCHA.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.kh.slumber.common.reCAPTCHA.dto.RecaptchaRequest;
import com.kh.slumber.common.reCAPTCHA.dto.RecaptchaResponse;

@RestController
public class RecaptchaController {
  // v2 시크릿 키
  private static final String RECAPTCHA_SECRET_V2 = "6Lc2o_cmAAAAAM0NJGNhnvOvZyc0bdNLHIZ63Gpc";

  @PostMapping("verify-recaptcha")
  public RecaptchaResponse verifyRecaptcha(@RequestBody RecaptchaRequest request) {

    RestTemplate restTemplate = new RestTemplate();
    String url = "https://www.google.com/recaptcha/api/siteverify";
    String params = "?secret=" + RECAPTCHA_SECRET_V2 + "&response=" + request.getResponse(); // 유저의 ip remoteip로 받을 수도
                                                                                             // 있지만 생략

    RecaptchaResponse response = restTemplate.postForObject(url + params, null, RecaptchaResponse.class);
    /*
     * 지금까지 만든게 무슨 뜻인가?
     * 1. RestTemplate을 이용해서 HTTP 요청을 POST 방식으로 보냅니다.
     * 2. reCAPTCHA의 검증 URL을 지정합니다. 이 url은 고정되어 있습니다. 공식 문서를 참고하세요.
     * 3. url에 붙일 파라미터를 지정합니다. 필수 파라미터인 secret과 response를 지정합니다.
     * secret은 reCAPTCHA를 발급받을 때 받은 키입니다. 필드로 선언되어있습니다.
     * response는 클라이언트에서 받아온 response입니다(즉, 사용자 응답).
     * 4. restTemplate.postForObject()를 이용해서 POST 요청을 보냅니다.
     * url+params는 검증 url과 매개변수를 결합한 문자열입니다.
     * null은 요청 본문을 나타내며,
     * RecaptchaResponse.class는 응답을 RecaptchaResponse 객체로 변환하라는 의미입니다.
     * 
     * 이 api는 다음과 같은 응답을 반환합니다.
     * {
     * "success": true|false, // 이 요청이 사이트에 대한 유효한 reCAPTCHA 토큰인지 여부
     * "challenge_ts": timestamp, // 챌린지 로드에 대한 timestamp (ISO format
     * yyyy-MM-dd'T'HH:mm:ssZZ) (유저가 테스트 한 시간)
     * "hostname": string, // reCAPTCHA가 해결된 사이트의 호스트 이름. 우리 사이트 이름이 들어감.
     * "error-codes": [...] // 옵션입니다. 받을수도 있고 안 받을 수도 있습니다.
     * }
     * 
     * error-codes는 다음과 같이 이루어집니다.
     * missing-input-secret secret 파라미터가 누락되었습니다. 발급받은 비밀 키가 없다
     * invalid-input-secret secret 파라미터가 잘못되었습니다. 발급받은 비밀 키가 잘못되었다
     * missing-input-response response 파라미터가 누락되었습니다. 사용자 응답이 없다
     * invalid-input-response response 파라미터가 잘못되었습니다. 사용자 응답이 잘못되었다
     * bad-request 요청이 잘못되었습니다. 요청이 잘못되었다
     * timeout-or-duplicate 응답이 만료되었거나 중복되었습니다. 응답이 만료되었거나 중복되었다
     * 
     * 원한다면 이러한 에러 코드를 처리할 수 있습니다.
     * 
     * System.out.println("========================================");
     * System.out.println("Response의 성공여부: " + response.isSuccess());
     * System.out.println("Response의 challenge_ts: " + response.getChallenge_ts());
     * System.out.println("Response의 hostname: " + response.getHostname());
     * System.out.println("Response의 error_codes: " + response.getError_codes());
     * System.out.println("========================================");
     */
    return response;
  }
}