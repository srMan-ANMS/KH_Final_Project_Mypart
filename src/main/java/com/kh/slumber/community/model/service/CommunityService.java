package com.kh.slumber.community.model.service;

import java.util.ArrayList;
import java.util.Map;

import com.kh.slumber.community.dto.CommunityPostAndWriterDTO;
import com.kh.slumber.community.dto.CommunityReplyAndWriterDTO;
import com.kh.slumber.community.model.vo.CommunityPost;
import com.kh.slumber.community.model.vo.CommunityPostAndWriter;
import com.kh.slumber.community.model.vo.CommunityReply;
import com.kh.slumber.member.model.vo.Member;



/**
 * Service의 인터페이스.
 * 직접 controller에 연결되는 메소드만 존재합니다.
 * 나머지 메소드는 Helper를 통해 호출됩니다.
 * 
 * 구분한 목적은 controller에서 어떤 메소드가 호출되는지 알기 위함입니다.
 * 다른 참여자들의 빠른 이해를 위해 구분했습니다.
 */
public interface CommunityService {

    Map<String, ArrayList<CommunityPostAndWriter>> getAllPostsCategorized();

    CommunityPostAndWriterDTO getPagedPostsAndWriters(String board, int page);

    CommunityPostAndWriter getPostByBoardId(String boardNo, Member loginUser, boolean visited);

    CommunityPostAndWriterDTO searchPagedPostsAndWriters(String board, int page, String searchOption, String searchKeyword);

    CommunityReplyAndWriterDTO getPagedRepliesAndWriters(String boardNo, int page);

    int addReply(CommunityReply reply);

    int likePost(String boardNo, String userNo);

    int dislikePost(String boardNo, String userNo);

    int editReply(CommunityReply reply);

    int deleteReply(Map<String, Object> requestData);

    int reportPost(String boardNo, String userNo);

    int likeReply(String userNo, String replyNo);

    int dislikeReply(String userNo, String replyNo);

    int reportReply(String userNo, String replyNo);

    int createPost(String content, String title, String category, String memberNo);

    int deletePost(int boardNo);

    CommunityPost getPost(int boardNo);

    int editPost(String content, String title, String category, String boardNo);
}
