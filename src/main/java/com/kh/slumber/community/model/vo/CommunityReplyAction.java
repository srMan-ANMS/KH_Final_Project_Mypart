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
public class CommunityReplyAction {
  private int memberNo;
  private int replyNo;
  private int isLiked;
  private int isDisliked;
  private int isReported;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    CommunityReplyAction that = (CommunityReplyAction) o;
    return memberNo == that.memberNo && replyNo == that.replyNo;
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberNo, replyNo);
  }
}
