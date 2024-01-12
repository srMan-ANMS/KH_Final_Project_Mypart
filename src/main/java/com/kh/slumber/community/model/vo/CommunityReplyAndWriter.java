package com.kh.slumber.community.model.vo;

import java.sql.Timestamp;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommunityReplyAndWriter {

  // rownum받아줌
  private int rownum;
  // 원래 테이블
  private int replyNo;
  private String memberNo;
  private int boardNo;
  private String replyContent;
  private int replyLikes;
  private int replyDislikes;
  private int replyReports;
  private Timestamp replyEnrollDate;
  private Timestamp replyModifyDate;
  private String replyIsDeleted;
  private String replyIsHidden;
  // 멤버관련부분 게시글에 필요한 것만 존재함.
  private String memberId;
  private String memberNickname;
  private String memberName;
  private String memberPhone;
  private Timestamp memberEnrollDate;
  private String memberStatus;
  private String isMemberBlacklisted;
  // 쓸데없는 건 안받았음.

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    CommunityReplyAndWriter that = (CommunityReplyAndWriter) o;
    return rownum == that.rownum &&
        replyNo == that.replyNo &&
        boardNo == that.boardNo &&
        replyLikes == that.replyLikes &&
        replyDislikes == that.replyDislikes &&
        replyReports == that.replyReports &&
        Objects.equals(memberNo, that.memberNo) &&
        Objects.equals(replyContent, that.replyContent) &&
        Objects.equals(replyEnrollDate, that.replyEnrollDate) &&
        Objects.equals(replyModifyDate, that.replyModifyDate) &&
        Objects.equals(replyIsDeleted, that.replyIsDeleted) &&
        Objects.equals(replyIsHidden, that.replyIsHidden) &&
        Objects.equals(memberId, that.memberId) &&
        Objects.equals(memberNickname, that.memberNickname) &&
        Objects.equals(memberName, that.memberName) &&
        Objects.equals(memberPhone, that.memberPhone) &&
        Objects.equals(memberEnrollDate, that.memberEnrollDate) &&
        Objects.equals(memberStatus, that.memberStatus) &&
        Objects.equals(isMemberBlacklisted, that.isMemberBlacklisted);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rownum, replyNo, memberNo, boardNo, replyContent, replyLikes, replyDislikes, replyReports,
        replyEnrollDate, replyModifyDate, replyIsDeleted, replyIsHidden, memberId, memberNickname, memberName,
        memberPhone, memberEnrollDate, memberStatus, isMemberBlacklisted);
  }
}
