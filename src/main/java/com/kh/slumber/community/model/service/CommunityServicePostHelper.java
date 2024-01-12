package com.kh.slumber.community.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kh.slumber.common.model.vo.PageInfo;
import com.kh.slumber.community.dto.CommunityPostAndWriterDTO;
import com.kh.slumber.community.model.dao.CommunityDAO;
import com.kh.slumber.community.model.vo.CommunityPostAndWriter;

/**
 * 이 클래스는, CommunityServiceImpl에서 사용하는 메소드(오버라이드 제외)중
 * 페이지에 관련된 것들을 정의한 클래스입니다.
 */
@Component
public class CommunityServicePostHelper {

  @Autowired
  private CommunityDAO dao;

  /**
   * boardType 게시판에서 전체 게시글 수량 세어옴
   */
  public int countAllPosts(String boardType) {
    return dao.countAllPosts(boardType);
  }

  /**
   * boardType 게시판에서 검색 조건에 맞는 게시글 수량 가져옴
   */
  public int countSearchedPosts(String boardType, String searchOption, String searchKeyword) {
    return dao.countSearchedPosts(boardType, searchOption, searchKeyword);
  }

  /**
   * boardType 게시판의 게시글과 그 작성자 목록을 가져옴. 
   * 페이징 필요해 PageInfo 받아서 RowBounds 처리함.
   */
  public ArrayList<CommunityPostAndWriter> getPostsAndWriters(PageInfo pi, String boardType) {
    return dao.getPostsAndWriters(getRowBounds(pi), boardType);
  }

  /**
   * boardType 게시판에서 게시글과 그것의 작성자 정보 목록을 가져옴
   */
  public ArrayList<CommunityPostAndWriter> getPostsAndWriters(String boardType) {
    return dao.getPostsAndWriters(boardType);
  }

  /**
   * CBAM DTO를 보내는 메소드
   */
  public CommunityPostAndWriterDTO sendCommunityPostAndWriterDTO(ArrayList<CommunityPostAndWriter> list,
      PageInfo pi, String board, int page) {
    if (list != null) {
      CommunityPostAndWriterDTO dto = new CommunityPostAndWriterDTO(list, pi, board, page);
      return dto;
    } else {
      return null;
    }
  }

  /**
   * boardType에서 검색 조건에 맞는 게시글과 작성자 목록을 가져옴
   */
  public ArrayList<CommunityPostAndWriter> searchPostsAndWriters(String board,
      String searchOption,
      String searchKeyword) {
    return dao.searchPostsAndWriters(board, searchOption, searchKeyword);
  }

  /**
   * boardType에서 검색 조건에 맞는 게시글과 작성자 목록을 가져옴.
   * 페이징 위해 PageInfo 받아서 RowBounds로 처리.
   */
  public ArrayList<CommunityPostAndWriter> searchPostsAndWriters(PageInfo pi, String boardType, String searchOption,
      String searchKeyword) {
    return dao.searchPostsAndWriters(getRowBounds(pi), boardType, searchOption, searchKeyword);
  }

  /**
   * PageInfo(페이지 정보)를 받아서 RowBounds 제작함.
   */
  public RowBounds getRowBounds(PageInfo pi) {
    int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
    return new RowBounds(offset, pi.getBoardLimit());
  }

  /**
   * boarNo 작성자 찾아옴.
   */
  public String getWriterMemberNo(String boardNo) {
    return String.valueOf(dao.getWriterMemberNo(boardNo).getMemberNo());
  }

  /**
   * boardNo의 조회수를 1 증가시킴
   */
  public void plusOneViewCount(String boardNo) {
    dao.plusOneViewCount(boardNo);
  }

  /**
   * userNo의 boardNo에 대한 actionType 행위를 토글함(했다면 안한걸로 안하면 한걸로)
   */
  public int insertPostReaderAction(String boardNo, String userNo, String actionType) {
    return dao.insertPostReaderAction(boardNo, userNo, actionType);
  }

  /**
   * userNo의 boardNo에 대한 actionType을 이전에 했는지 확인함.
   */
  public int checkPostReaderAction(String boardNo, String userNo, String actionType) {
    return dao.checkPostReaderAction(boardNo, userNo, actionType);
  }

  /**
   * boardNo에서 유저의 actionType행위를 -1함.
   */
  public int minusOneToPostReaderAction(String boardNo, String actionType) {
    return dao.minusOneToPostReaderAction(boardNo, actionType);
  }

  /**
   * boardNo에서 유저의 actionType행위를 +1함.
   */
  public int plusOneToPostReaderAction(String boardNo, String actionType) {
    return dao.plusOneToPostReaderAction(boardNo, actionType);
  }

  /**
   * BoardNo의 ActionType에 대한  userNo의 행위를 처리함.
   * 추천, 반대, 신고의 반복되는 로직 처리하기 위해 존재함.
   * @param boardNo
   * @param userNo
   * @param actionType : like, dislike, report... 원하면 추가 가능
   * @return : 어떤 행위를 토글에 성공하면 1, 취소하면 0, 에러 발생하면 -1을 반환함.
   */
  public int handlePostReaderAction(String boardNo, String userNo, String actionType) {
    //1. 토글하려면 일단 이전에 무슨 행동을 실행 했었는지, 안 했는지 확인, 이전에 실행을 했다면 실행한 적이 있다고 추가하고, 아니면 삭제함.
    int checkReturn = checkPostReaderAction(boardNo, userNo, actionType);
    if (checkReturn == 1) {                                                           // 행위를 이전에 한 적이 있다면
      int deleteReturn = deletePostReaderAction(boardNo, userNo, actionType);         // 토글이 제대로 실행되었는지 확인. 토글에 성공하면 1, 실패하면 0을 반환함.
      if (deleteReturn == 0) {
        return -1;
      }

      int minusReturn = minusOneToPostReaderAction(boardNo, actionType);
      if (minusReturn == 0) {                                                          // -1에 성공했는지 확인함.
        // 에러 발생
        return -1;
      }

      return 0;                                                                        // 토글이 제대로 실행되었는지 2번 확인했으니, 행동을 취소할 수 있도록 0을 반환함.
    } else if (checkReturn == 0) {                                                     // 행위한 적이 없었다면 
      int toggleReturn = insertPostReaderAction(boardNo, userNo, actionType);
      if (toggleReturn == 0) {                                                         // 에러 발생
        return -1;
      }
      
      int plusReturn = plusOneToPostReaderAction(boardNo, actionType);                 //행위 +1
      if (plusReturn == 0) {                                                           // 에러 발생
        return -1;
      }
      return 1;
    } else {                                                                            // checkPostReaderAction 에러 발생
      return -1;
    }
  }

  private int deletePostReaderAction(String boardNo, String userNo, String actionType) {
    return dao.deletePostReaderAction(boardNo, userNo, actionType);
  }
}
