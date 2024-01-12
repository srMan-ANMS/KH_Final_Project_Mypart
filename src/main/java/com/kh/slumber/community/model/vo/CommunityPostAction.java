package com.kh.slumber.community.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommunityPostAction {
  private int memberNo;
  private int boardNo;
  private int isLiked;
  private int isDisliked;
  private int isReported;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    CommunityPostAction that = (CommunityPostAction) o;
    return memberNo == that.memberNo &&
        boardNo == that.boardNo &&
        isLiked == that.isLiked &&
        isDisliked == that.isDisliked &&
        isReported == that.isReported;
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberNo, boardNo, isLiked, isDisliked, isReported);
  }
}
