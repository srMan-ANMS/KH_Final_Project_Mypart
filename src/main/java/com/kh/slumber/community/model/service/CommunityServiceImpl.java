package com.kh.slumber.community.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.slumber.common.Pagination;
import com.kh.slumber.common.model.vo.PageInfo;
import com.kh.slumber.community.dto.CommunityPostAndWriterDTO;
import com.kh.slumber.community.dto.CommunityReplyAndWriterDTO;
import com.kh.slumber.community.model.dao.CommunityDAO;
import com.kh.slumber.community.model.vo.CommunityPost;
import com.kh.slumber.community.model.vo.CommunityPostAndWriter;
import com.kh.slumber.community.model.vo.CommunityReply;
import com.kh.slumber.community.model.vo.CommunityReplyAndWriter;
import com.kh.slumber.member.model.vo.Member;


/**
 * dao에 접근하는 service
 * service 내에서 많이 쓰이는 로직은 private 함수로 따로 구현했습니다.
 */
@Service
public class CommunityServiceImpl implements CommunityService {

  @Autowired
  private CommunityDAO dao;

  @Autowired
  private CommunityServicePostHelper postHelper;

  @Autowired
  private CommunityServiceReplyHelper replyHelper;

  /**
   * 각 게시판별 게시글을 가져오는 메소드. 이거 너무 많이 안가져오도록 나중에 로직 수정하세요.
   */
  @Override
  public Map<String, ArrayList<CommunityPostAndWriter>> getAllPostsCategorized() {
    Map<String, ArrayList<CommunityPostAndWriter>> map = new HashMap<String, ArrayList<CommunityPostAndWriter>>();
    
    //게시글 10개만 가져오면 되니까
    PageInfo pi = Pagination.getPageInfo(1, 10, 10);

    map.put("best", postHelper.getPostsAndWriters(pi, "best"));
    map.put("free_and_humor", postHelper.getPostsAndWriters(pi, "free_and_humor"));
    map.put("tips_and_info", postHelper.getPostsAndWriters(pi, "tips_and_info"));
    // map.put("review", postHelper.getPostsAndWriters(pi, "review"));
    return map;
  }

  /**
   * boardType 게시판의 모든 게시글과 그 작성자 정보를 페이징해 가져옴
   */
  @Override
  public CommunityPostAndWriterDTO getPagedPostsAndWriters(String boardType, int page) {

    PageInfo pi = Pagination.getPageInfo(page, postHelper.countAllPosts(boardType), 20);
    ArrayList<CommunityPostAndWriter> list = postHelper.getPostsAndWriters(pi, boardType);

    return postHelper.sendCommunityPostAndWriterDTO(list, pi, boardType, page);
  }

  /**
   * boardType 게시판의 게시글과 그 작성자 정보를 검색 조건에 맞게 검색한 것을 페이징해 가져옴.
   */
  @Override
  public CommunityPostAndWriterDTO searchPagedPostsAndWriters(String boardType, int page, String searchOption,
      String searchKeyword) {
    // 페이징 처리하고
    PageInfo pi = Pagination.getPageInfo(page, postHelper.countSearchedPosts(boardType, searchOption, searchKeyword), 20);
    // 그걸로 검색하고
    ArrayList<CommunityPostAndWriter> list = postHelper.searchPostsAndWriters(pi, boardType, searchOption, searchKeyword);
    // 컨트롤러에 넘긴다.
    return postHelper.sendCommunityPostAndWriterDTO(list, pi, boardType, page);
  }

  /**
   * boardNo를 상세조회함. 이 때 작성자가 loginUser이면, 조회수를 올리지 않음.
   */
  @Override
  public CommunityPostAndWriter getPostByBoardId(String boardNo, Member loginUser, boolean visited) {
    String writer = postHelper.getWriterMemberNo(boardNo);
    if (loginUser != null && !(loginUser.getMemberNo().equals(writer)) && !visited) {
      System.out.println("조회수 올리기");
      System.out.println("loginUser.getMemberNo() : " + loginUser.getMemberNo());
      System.out.println("writer : " + writer);
      System.out.println("loginUser: " + loginUser);
      System.out.println("boardNo: " + boardNo);
      System.out.println("visited: " + visited);
      postHelper.plusOneViewCount(boardNo);
    }
    return dao.getPostByBoardId(boardNo);
  }

  /**
   * boardNo의 댓글 페이징처리해서 가져옴.
   */
  @Override
  public CommunityReplyAndWriterDTO getPagedRepliesAndWriters(String boardNo, int page) {
    PageInfo pi = Pagination.getPageInfo(page, replyHelper.countAllReplies(boardNo), 20);
    ArrayList<CommunityReplyAndWriter> list = replyHelper.getRepliesAndWriters(pi, boardNo);
    CommunityReplyAndWriterDTO cdto = replyHelper.sendCommmunityReplyAndWriterDTO(list, pi, boardNo, page);
    return cdto;
  }

  /**
   * 댓글을 추가함
   */
  @Override
  public int addReply(CommunityReply reply) {
    return dao.addReply(reply);
  }

  /**
   * boardNo에 userNo의 추천 행위 처리함.
   * 추천하면 1, 추천 취소하면 0, 에러가 발생하면 -1 리턴함.
   */
  @Override
  public int likePost(String boardNo, String userNo) {
    return postHelper.handlePostReaderAction(boardNo, userNo, "like");
  }

  @Override
  public int dislikePost(String boardNo, String userNo) {
    return postHelper.handlePostReaderAction(boardNo, userNo, "dislike");
  }

  @Override
  public int reportPost(String boardNo, String userNo) {
    return postHelper.handlePostReaderAction(boardNo, userNo, "report");
  }

  @Override
  public int editReply(CommunityReply reply) {
    return dao.editReply(reply);
  }

  @Override
  public int deleteReply(Map<String, Object> requestData) {
    return dao.deleteReply(requestData);
  }

  @Override
  public int likeReply(String userNo, String replyNo) {
    return replyHelper.handleReplyReaderAction(replyNo, userNo, "like");
  }

  @Override
  public int dislikeReply(String userNo, String replyNo) {
    return replyHelper.handleReplyReaderAction(replyNo, userNo, "dislike");
  }

  @Override
  public int reportReply(String userNo, String replyNo) {
    return replyHelper.handleReplyReaderAction(replyNo, userNo, "report");
  }

  @Override
  public int createPost(String content, String title, String category, String memberNo) {
    return dao.createPost(content, title, category, memberNo);
  }

  @Override
  public int deletePost(int boardNo) {
    return dao.deletePost(boardNo);
  }

  @Override
  public CommunityPost getPost(int boardNo) {
    return dao.getPost(boardNo);
  }

  @Override
  public int editPost(String content, String title, String category, String boardNo) {
    return dao.editPost(content, title, category, boardNo);
  }

}
