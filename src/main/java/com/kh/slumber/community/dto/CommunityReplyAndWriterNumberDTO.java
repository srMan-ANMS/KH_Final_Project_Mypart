package com.kh.slumber.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * controller-service간 데이터 전송을 위한 객체.
 * DTO 설계시, 데이터의 무결성을 보장하기 위해 SETTER 사용하지 않음.
 */
@Getter
// @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommunityReplyAndWriterNumberDTO {

  private String memberNo;
  private String replyNo;
  
}
