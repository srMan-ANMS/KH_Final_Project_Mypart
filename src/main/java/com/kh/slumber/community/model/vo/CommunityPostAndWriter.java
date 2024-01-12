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
public class CommunityPostAndWriter {
  // rowNum받아줌
  private int rownum;
  // 보드관련부분
  private int boardNo;
  private int memberNo;
  private String boardTitle;
  private String boardContent;
  private int boardLikes;
  private int boardDislikes;
  private int boardReports;
  private int boardViews;
  private String boardType;
  private Timestamp boardEnrollDate;
  private Timestamp boardModifyDate;
  private String boardIsDeleted;
  private String boardIsHidden;

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
    CommunityPostAndWriter that = (CommunityPostAndWriter) o;
    return boardNo == that.boardNo &&
        memberNo == that.memberNo &&
        boardLikes == that.boardLikes &&
        boardDislikes == that.boardDislikes &&
        boardReports == that.boardReports &&
        boardViews == that.boardViews &&
        Objects.equals(boardTitle, that.boardTitle) &&
        Objects.equals(boardContent, that.boardContent) &&
        Objects.equals(boardType, that.boardType) &&
        Objects.equals(boardEnrollDate, that.boardEnrollDate) &&
        Objects.equals(boardModifyDate, that.boardModifyDate) &&
        Objects.equals(boardIsDeleted, that.boardIsDeleted) &&
        Objects.equals(boardIsHidden, that.boardIsHidden) &&
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
    return Objects.hash(boardNo, memberNo, boardTitle, boardContent, boardLikes, boardDislikes, boardReports,
        boardViews, boardType, boardEnrollDate, boardModifyDate, boardIsDeleted, boardIsHidden, memberId,
        memberNickname, memberName, memberPhone, memberEnrollDate, memberStatus, isMemberBlacklisted);
  }
}
