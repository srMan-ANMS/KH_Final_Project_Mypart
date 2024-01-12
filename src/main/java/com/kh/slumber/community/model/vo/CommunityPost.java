package com.kh.slumber.community.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommunityPost {
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

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    CommunityPost that = (CommunityPost) o;
    return boardNo == that.boardNo;
  }

  @Override
  public int hashCode() {
    return Objects.hash(boardNo);
  }
}
