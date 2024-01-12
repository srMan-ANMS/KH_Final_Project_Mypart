/*
 * SPRING MVC의 Controller 클래스입니다.
 * 가급적 주석을 간단히 달아, 다른 프로젝트 참여자분들이 이해하기 쉽게 했습니다.
 */

package com.kh.slumber.community.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.kh.slumber.community.dto.CommunityPostAndWriterDTO;
import com.kh.slumber.community.dto.CommunityReplyAndWriterDTO;
import com.kh.slumber.community.model.service.CommunityService;
import com.kh.slumber.community.model.vo.CommunityPost;
import com.kh.slumber.community.model.vo.CommunityPostAndWriter;
import com.kh.slumber.community.model.vo.CommunityReply;
import com.kh.slumber.member.model.vo.Member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SessionAttributes("loginUser")
@Controller
public class CommunityController {

  @Autowired
  private CommunityService service;

  @Autowired
  private TemplateEngine templateEngine; // ajax 요청 시, thymeleaf를 html을 문자열로 변환해주는 객체

  /**
   * 커뮤니티 메인으로 이동
   * 
   * @param model
   * @return comm_main.html
   */
  @GetMapping(value = "main.comm")
  public String moveCommunity(
      Model model) {

    Map<String, ArrayList<CommunityPostAndWriter>> boardMap = service.getAllPostsCategorized();
    // service와 딱 한번 통신하면서 dao를 3번 작동시켜야 하므로, Map으로 데이터를 받아온다.

    if (boardMap != null) {
      // 메인에서는 공지사항과 문의사항을 리스트로 표시할 필요 없어서, 모델에 추가하지 않음
      model.addAttribute("bestList", boardMap.get("best"));
      model.addAttribute("freeList", boardMap.get("free_and_humor"));
      model.addAttribute("infoList", boardMap.get("tips_and_info"));
      return "comm_main";
    } else {
      return "error";
    }
  }

  /**
   * 커뮤니티 게시판으로 이동
   * 게시판 이동 시 검색하는 경우도 있으므로
   * 검색 옵션과 검색어를 받을 수 있다.
   * 
   * @param board         게시판 이름
   * @param searchOption  검색 옵션
   * @param searchKeyword 검색어
   * @param page          페이지 번호
   * @param model
   * @return comm_board.html
   */
  @GetMapping(value = "board.comm")
  public String moveBoard(@RequestParam("board") String board,
      @RequestParam(value = "searchOption", required = false) String searchOption,
      @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      Model model) {

    CommunityPostAndWriterDTO dto = null; // 게시글과 그 작성자의 정보를 담는 DTO
    if (searchOption == null) { // 검색어 옵션이 없으면, 전체 게시글 목록
      dto = service.getPagedPostsAndWriters(board, page);
    } else if (searchOption.equals("title") || searchOption.equals("content") || searchOption.equals("author")
        || searchOption.equals("titleAndContent") || searchOption.equals("all")) { // 검색어 옵션이 검색 가능한 것인지 검증하고,
      if (searchKeyword.length() < 2) { // 또한 길이가 2글자 미만이면 에러.
        return "error";
      }
      dto = service.searchPagedPostsAndWriters(board, page, searchOption, searchKeyword); // 검색어 2글자 이상, 옵션 검증 완료 시, 검색
    } else {
      return "error";
    }

    if (dto != null) {
      model.addAttribute("board", dto.getBoard());
      model.addAttribute("page", dto.getPage());
      model.addAttribute("list", dto.getList());
      model.addAttribute("pi", dto.getPi());
      return "comm_board";
    } else {
      return "error";
    }
  }

  /**
   * 커뮤니티 글쓰기로 이동
   * 
   * @param board
   * @return comm_write.html
   */
  @GetMapping("write.comm")
  public String moveWrite(
      @RequestParam("board") String board,
      Model model,
      HttpSession session) {

    Member loginUser = (Member) session.getAttribute("loginUser");

    if (loginUser == null) {
      return "redirect:/loginView.me"; // Redirect to login page if user is not logged in
    }

    model.addAttribute("board", board);
    return "comm_write";
  }

  /**
   * 게시글 상세로 이동.
   * 
   * @param boardNo 게시글 번호
   * @param page    페이지 번호
   * @param model
   * @param session
   * @return comm_post.html
   */
  @GetMapping(value = "post.comm")
  public String movePost(
      @RequestParam("no") String boardNo,
      @RequestParam(value = "page", required = false, defaultValue = "1") int page,
      Model model,
      HttpSession session,
      HttpServletResponse response,
      HttpServletRequest request) {

    Member loginUser = (Member) session.getAttribute("loginUser");
    String memberId = loginUser != null ? loginUser.getMemberId() : "no-login";
    String cookieName = "visited_" + boardNo + "_" + memberId; // 쿠키 이름은 visited_게시글번호_memberId(만약 로그인아니라면 no login)
    Cookie[] cookies = request.getCookies(); // 쿠키를 받아옵니다.
    Cookie cookie = null;
    boolean visited = false; // visited는 false로 초기화합니다.

    if (cookies != null) { // 쿠키가 null이 아니면,
      for (Cookie eachCookie : cookies) { // 쿠키를 하나씩 검사합니다.
        if (eachCookie.getName().equals(cookieName)) { // 쿠키 이름이 visited_게시글번호인 쿠키가 있다면,
          visited = true; // visited를 true로 바꿉니다.
          break; // for문을 빠져나옵니다.
        }
      }
    }
    if (!visited) {
      cookie = new Cookie(cookieName, "true"); // 쿠키 이름이 visited_게시글번호인 쿠키가 없다면, 쿠키를 만듭니다.
      cookie.setMaxAge(60 * 60 * 24); // 쿠키의 유효기간은 하루입니다.
      response.addCookie(cookie); // 쿠키를 추가합니다.
    }

    CommunityPostAndWriter cpaw = service.getPostByBoardId(boardNo, loginUser, visited);
    // 이 서비스는, 조회수 검증 서비스와 게시글 조회 서비스를 실행합니다(즉 2개 실행합니다.)
    if (cpaw != null) {
      model.addAttribute("cpaw", cpaw);
      return "comm_post";
    } else {
      return "error";
    }
  }

  /**
   * 댓글 조회에 대한 AJAX 처리용
   * Thymeleaf는 서버에서 html을 만들어서 클라이언트에게 전달하는 방식이므로,
   * 서버에서 html로 파싱하는 과정을 포함함.
   * 
   * @param boardNo
   * @param userNo
   * @param session
   * @return
   */
  @GetMapping("replyList")
  @ResponseBody
  public ResponseEntity<String> getPagedRepliesAndWriters(
      @RequestParam("boardNo") String boardNo,
      @RequestParam(value = "page", required = false) int page,
      HttpSession session) {

    Member loginUser = (Member) session.getAttribute("loginUser"); // session에서 loginUser
    CommunityReplyAndWriterDTO craw = service.getPagedRepliesAndWriters(boardNo, page); // service에서 댓글 목록

    // Thymeleaf를 이용해 html 만드는 과정
    Context context = new Context(); // thymeleaf에서 사용할 context를 만듭니다.
    context.setVariable("replies", craw.getList()); // context에 craw에서 받아온 댓글 목록을 넣습니다. reply라는 이름으로 넣습니다.
    context.setVariable("loginUser", loginUser); // context에 loginUser를 넣습니다.
    context.setVariable("pi", craw.getPi()); // context에 craw에서 받아온 pageInfo을 넣습니다.
    context.setVariable("boardNo", boardNo); // context에 boardNo를 넣습니다.
    String html = templateEngine.process("replyList", context); // thymeleaf를 이용해 html을 만듭니다.

    return ResponseEntity.ok(html); // html을 반환합니다.
  }

  @PostMapping("addReply")
  @ResponseBody
  public Map<String, Object> addReply(
      @RequestBody CommunityReply reply) {

    int result = service.addReply(reply); // 댓글 등록 로직 수행 : reply의 일부인 memberNo, boardNo, replyContent 삽입하는 service 호출
    Map<String, Object> response = new HashMap<>(); // 잘 만들어졌다면 Map인 response에 statusCode를 1이 들어가서 반환될 것입니다.
    response.put("statusCode", result);
    return response;
  }

  // 아래부터 댓글/ 본문 좋아요, 싫어요, 신고 기능에 대한 컨트롤러 메소드입니다. 기능은 비슷합니다.
  @PostMapping("likePost")
  @ResponseBody
  public ResponseEntity<?> likePost(
      @RequestParam("boardNo") String boardNo,
      @RequestParam("userNo") String userNo) {

    int result = service.likePost(boardNo, userNo);
    // 1을 받으면 추천 성공하고, 0을 받으면 추천 취소 성공하고, -1을 받으면 추천 실패입니다.
    return getServiceUserActionResult(result, 1, 0, -1);
  }

  @PostMapping("dislikePost")
  @ResponseBody
  public ResponseEntity<?> dislikePost(
      @RequestParam("boardNo") String boardNo,
      @RequestParam("userNo") String userNo) {

    int result = service.dislikePost(boardNo, userNo);
    // 1을 받으면 반대 성공하고, 0을 받으면 반대 취소 성공하고, -1을 받으면 반대 실패입니다.
    return getServiceUserActionResult(result, 1, 0, -1);
  }

  @PostMapping("reportPost")
  @ResponseBody
  public ResponseEntity<?> reportPost(
      @RequestParam("boardNo") String boardNo,
      @RequestParam("userNo") String userNo) {

    int result = service.reportPost(boardNo, userNo);
    // 1을 받으면 신고 성공하고, 0을 받으면 신고 취소 성공하고, -1을 받으면 신고 실패입니다.
    return getServiceUserActionResult(result, 1, 0, -1);
  }

  @PostMapping("likeReply")
  @ResponseBody
  public ResponseEntity<?> likeReply(
      @RequestParam("replyNo") String replyNo,
      @RequestParam("memberNo") String memberNo) {

    int result = service.likeReply(memberNo, replyNo);
    // 1을 받으면 성공, 0 받으면 취소 성공, -1을 받으면 실패
    return getServiceUserActionResult(result, 1, 0, -1);
  }

  @PostMapping("dislikeReply")
  @ResponseBody
  public ResponseEntity<?> dislikeReply(
      @RequestParam("replyNo") String replyNo,
      @RequestParam("memberNo") String memberNo) {

    int result = service.dislikeReply(memberNo, replyNo);
    return getServiceUserActionResult(result, 1, 0, -1);
  }

  @PostMapping("reportReply")
  @ResponseBody
  public ResponseEntity<?> reportReply(
      @RequestParam("replyNo") String replyNo,
      @RequestParam("memberNo") String memberNo) {

    int result = service.reportReply(memberNo, replyNo);
    return getServiceUserActionResult(result, 1, 0, -1);
  }

  /**
   * 댓글 수정하는 메소드
   * 
   * @param reply
   * @return
   */
  @PostMapping("editReply")
  @ResponseBody
  public Map<String, Object> editReply(
      @RequestBody CommunityReply reply) {

    int result = service.editReply(reply);

    // 잘 만들어졌다면 Map인 response에 statusCode를 1이 들어가서 반환될 것입니다.
    Map<String, Object> response = new HashMap<>();
    response.put("statusCode", result);
    return response;
  }

  /**
   * 댓글 삭제
   * 
   * @param requestData
   * @return
   */
  @PostMapping("deleteReply")
  @ResponseBody
  public Map<String, Object> deleteReply(
      @RequestBody Map<String, Object> requestData) {

    int result = service.deleteReply(requestData);

    // 잘 만들어졌다면 Map인 response에 statusCode를 1이 들어가서 반환될 것입니다.
    Map<String, Object> response = new HashMap<>();
    response.put("statusCode", result);
    return response;
  }

  @PostMapping("commPostDelete")
  @ResponseBody
  public ResponseEntity<Map<String, String>> deletePost(
      @RequestParam("boardNo") int boardNo) {

    Map<String, String> resultMap = new HashMap<>();
    int rowsAffected = service.deletePost(boardNo);
    if (rowsAffected == 1) {
      resultMap.put("result", "success");
      return ResponseEntity.ok(resultMap);
    } else {
      resultMap.put("result", "fail");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resultMap);
    }
  }

  /**
   * 게시글 제출하기
   * 
   * @param content
   * @param title
   * @param category
   * @param memberNo
   * @return
   */
  @PostMapping("commPostSubmit")
  @ResponseBody
  public ResponseEntity<Map<String, String>> commPostSubmit(
      @RequestParam("content") String content,
      @RequestParam("title") String title,
      @RequestParam("category") String category,
      @RequestParam("memberNo") String memberNo) {

    Map<String, String> result = new HashMap<>();
    int rowsAffected = service.createPost(content, title, category, memberNo);
    if (rowsAffected > 0) {
      result.put("result", "success");
      return ResponseEntity.ok(result);
    } else {
      result.put("result", "fail");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
  }

  /**
   * 수정한 게시글 제출하기
   * 
   * @param content
   * @param title
   * @param category
   * @param memberNo
   * @return
   */
  @PostMapping("commEditedPostSubmit")
  @ResponseBody
  public ResponseEntity<Map<String, String>> commEditedPostSubmit(
      @RequestParam("content") String content,
      @RequestParam("title") String title,
      @RequestParam("category") String category,
      @RequestParam("boardNo") String boardNo) {

    Map<String, String> result = new HashMap<>();
    int rowsAffected = service.editPost(content, title, category, boardNo);
    if (rowsAffected > 0) {
      result.put("result", "success");
      return ResponseEntity.ok(result);
    } else {
      result.put("result", "fail");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
  }

  /**
   * 게시글 수정 페이지로 넘어가기
   * 
   * @param boardNo
   * @param session
   * @param model
   * @return
   */
  @GetMapping("commPostEdit")
  public String commPostEdit(@RequestParam("boardNo") int boardNo, HttpSession session, Model model) {

    // 현재 session에서의 사용자와, 게시글 작성자가 같은지 확인
    Member loginUser = (Member) session.getAttribute("loginUser");
    CommunityPost post = service.getPost(boardNo);

    if (loginUser == null) {
      return "redirect:loginView.me";
    } else if (!loginUser.getMemberNo().equals(Integer.toString(post.getMemberNo()))) {
      return "redirect:main.comm";
    } else if (loginUser.getMemberNo().equals(Integer.toString(post.getMemberNo()))) {
      model.addAttribute("post", post);
      return "comm_edit";
    } else {
      return "error";
    }
  }

  // private
  // 메소드========================================================================

  /**
   * 게시글 좋아요, 싫어요, 신고, 댓글 좋아요, 싫어요, 신고에 대한 결과를 반환하는 메소드
   * 받아온 result에 대해 결과를 반환합니다.
   * 
   * @param result         서비스로부터 1, 0, -1 중 하나를 받습니다. 다른 걸 받도록 설계 가능합니다.
   * @param successMessage 성공 메세지는 어느 정수으로 할 것인지.
   * @param cancelMessage  취소 성공은 어느 정수인지
   * @param failureMessage 실패는 어느 정수인지
   * @return
   */
  private ResponseEntity<?> getServiceUserActionResult(int result, int successMessage, int cancelMessage,
      int failureMessage) {
    if (result == 1) {
      return ResponseEntity.ok(successMessage);
    } else if (result == 0) {
      return ResponseEntity.ok(cancelMessage);
    } else {
      return ResponseEntity.badRequest().body(failureMessage);
    }
  }
}