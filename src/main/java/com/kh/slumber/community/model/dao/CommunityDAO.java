package com.kh.slumber.community.model.dao;

import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.kh.slumber.community.model.vo.CommunityPost;
import com.kh.slumber.community.model.vo.CommunityPostAndWriter;
import com.kh.slumber.community.model.vo.CommunityReply;
import com.kh.slumber.community.model.vo.CommunityReplyAndWriter;

/**
 * DAO는 하나의 service에만 매핑되도록 함.
 * 필요한 상황을 대비해 overload한 메소드 존재함.
 */
@Mapper
public interface CommunityDAO {

    public ArrayList<CommunityPost> getPosts(String string);

    public int countAllPosts(String board);

    public int countSearchedPosts(
            @Param("board") String board,
            @Param("searchOption") String searchOption,
            @Param("searchKeyword") String searchKeyword);

    public ArrayList<CommunityPostAndWriter> getPostsAndWriters(String board);

    public ArrayList<CommunityPostAndWriter> getPostsAndWriters(RowBounds rowBounds, String board);

    public CommunityPostAndWriter getWriterMemberNo(String boardNo);

    public void plusOneViewCount(String boardNo);

    public CommunityPostAndWriter getPostByBoardId(String boardNo);

    public ArrayList<CommunityPostAndWriter> searchPostsAndWriters(
            @Param("board") String board,
            @Param("searchOption") String searchOption,
            @Param("searchKeyword") String searchKeyword);

    public ArrayList<CommunityPostAndWriter> searchPostsAndWriters(
            RowBounds rowBounds,
            @Param("board") String board,
            @Param("searchOption") String searchOption,
            @Param("searchKeyword") String searchKeyword);

    public int checkPostReaderAction(
            @Param("boardNo") String boardNo,
            @Param("userNo") String userNo,
            @Param("actionType") String actionType);

    public int insertPostReaderAction(
            @Param("boardNo") String boardNo,
            @Param("userNo") String userNo,
            @Param("actionType") String actionType);

    public ArrayList<CommunityReplyAndWriter> getRepliesAndWriters(RowBounds rowBounds, String boardNo);

    public int countAllReplies(String boardNo);

    public int addReply(CommunityReply reply);

    public int editReply(CommunityReply reply);

    public int deleteReply(Map<String, Object> requestData);

    public int checkReplyReaderAction(
            @Param("replyNo") String replyNo,
            @Param("userNo") String userNo,
            @Param("actionType") String actionType);

    public int insertReplyReaderAction(
            @Param("replyNo") String replyNo,
            @Param("userNo") String userNo,
            @Param("actionType") String actionType);

    public int createPost(
            @Param("content") String content,
            @Param("title") String title,
            @Param("category") String category,
            @Param("memberNo") String memberNo);

    public int plusOneToPostReaderAction(
            @Param("boardNo") String boardNo,
            @Param("actionType") String actionType);

    public int plusOneToReplyReaderAction(
            @Param("replyNo") String replyNo,
            @Param("actionType") String actionType);

    public int minusOneToPostReaderAction(
            @Param("boardNo") String boardNo,
            @Param("actionType") String actionType);

    public int minusOneToReplyReaderAction(
            @Param("replyNo") String replyNo,
            @Param("actionType") String actionType);

    public int deletePostReaderAction(
            @Param("boardNo") String boardNo,
            @Param("memberNo") String userNo,
            @Param("actionType") String actionType);

    public int deleteReplyReaderAction(
            @Param("replyNo") String replyNo,
            @Param("memberNo") String userNo,
            @Param("actionType") String actionType);

    public int deletePost(int boardNo);

    public CommunityPost getPost(int boardNo);

    public int editPost(
            @Param("content") String content,
            @Param("title") String title,
            @Param("category") String category,
            @Param("boardNo") String boardNo);
}