package com.kh.slumber.community.model.vo;

import java.sql.Timestamp;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommunityReply {
  // 원래 테이블
  private int replyNo;
  private int memberNo;
  private int boardNo;
  private String replyContent;
  private int replyLikes;
  private int replyDislikes;
  private int replyReports;
  private Timestamp replyEnrollDate;
  private Timestamp replyModifyDate;
  private String replyIsDeleted;
  private String replyIsHidden;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    CommunityReply that = (CommunityReply) o;
    return replyNo == that.replyNo;
  }

  @Override
  public int hashCode() {
    return Objects.hash(replyNo);
  }
}