package com.kh.slumber.community.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kh.slumber.common.model.vo.PageInfo;
import com.kh.slumber.community.dto.CommunityReplyAndWriterDTO;
import com.kh.slumber.community.model.dao.CommunityDAO;
import com.kh.slumber.community.model.vo.CommunityReplyAndWriter;

@Component
public class CommunityServiceReplyHelper {

  @Autowired
  private CommunityDAO dao;
  /**
   * RowBounds를 만들어주는 메소드
   */
  public RowBounds getRowBounds(PageInfo pi) {
    int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
    return new RowBounds(offset, pi.getBoardLimit());
  }

  /**
   * 특정 게시글의 전체 댓글 목록을 가져옴. 페이징처리함.
   */
  public ArrayList<CommunityReplyAndWriter> getRepliesAndWriters(PageInfo pi, String boardNo) {
    return dao.getRepliesAndWriters(getRowBounds(pi), boardNo);
  }

  /**
   * 특정 게시글의 전체 댓글 수를 측정.
   */
  public int countAllReplies(String boardNo) {
    return dao.countAllReplies(boardNo);
  }

  /**
   * CRAW DTO 전송함.
   */
  public CommunityReplyAndWriterDTO sendCommmunityReplyAndWriterDTO(ArrayList<CommunityReplyAndWriter> list,
      PageInfo pi, String boardNo, int page) {
    if (list != null) {
      CommunityReplyAndWriterDTO dto = new CommunityReplyAndWriterDTO(list, pi, boardNo, page);
      return dto;
    } else {
      return null;
    }
  }

  /**
   * 댓글에 대한 특정 행동 취한 적 있는지 확인
   */
  public int checkReplyReaderAction(String replyNo, String userNo, String actionType) {
    return dao.checkReplyReaderAction(replyNo, userNo, actionType);
  }

  public int minusOneToReplyReaderAction(String replyNo, String actionType) {
    return dao.minusOneToReplyReaderAction(replyNo, actionType);
  }

  public int plusOneToReplyReaderAction(String replyNo, String actionType) {
    return dao.plusOneToReplyReaderAction(replyNo, actionType);
  }

  public int insertReplyReaderAction(String replyNo, String userNo, String actionType) {
    return dao.insertReplyReaderAction(replyNo, userNo, actionType);
  }

  public int deleteReplyReaderAction(String boardNo, String userNo, String actionType) {
    return dao.deleteReplyReaderAction(boardNo, userNo, actionType);
  }

  /**
   * replyNo의 ActionType에 대한 userNo의 행위를 처리함.
   * 추천, 반대, 신고의 반복되는 로직 처리하기 위해 존재함.
   * @param boardNo
   * @param userNo
   * @param actionType : like, dislike, report... 원하면 추가 가능
   * @return : 어떤 행위를 토글에 성공하면 1, 취소하면 0, 에러 발생하면 -1을 반환함.
   */
  public int handleReplyReaderAction(String replyNo, String userNo, String actionType) {
    int checkReturn = checkReplyReaderAction(replyNo, userNo, actionType);
    if (checkReturn == 1) {
      //이전행위 존재시 행위 취소
      int deleteReturn = deleteReplyReaderAction(replyNo, userNo, actionType);
      // 토글이 제대로 실행되었는지 확인. 토글에 성공하면 1, 실패하면 0을 반환함.
      if (deleteReturn == 0) {
        return -1;
      }
      int minusReturn = minusOneToReplyReaderAction(replyNo, actionType);
      if (minusReturn == 0) {
        return -1;
      }

      return 0;
    } else if (checkReturn == 0) {
      // 추천한 적이 없으므로 좋아요를 눌렀다고 toggle하고, 추천 수를 1 증가시킴.
      int insertReturn = insertReplyReaderAction(replyNo, userNo, actionType);
      if (insertReturn == 0) {
        return -1;
      }
      int plusReturn = plusOneToReplyReaderAction(replyNo, actionType);
      if (plusReturn == 0) {
        return -1;
      }
      return 1;
    } else {
      return -1;
    }
  }
}
