package com.samsung.framework.controller.board;

import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.controller.common.ParentController;
import com.samsung.framework.domain.board.Board;
import com.samsung.framework.domain.board.BoardPublic;
import com.samsung.framework.domain.common.Paging;
import com.samsung.framework.vo.board.BoardPublicVO;
import com.samsung.framework.vo.board.BoardReplyVO;
import com.samsung.framework.vo.code.CommonCodeVO;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.search.board.BoardSearchVO;
import com.samsung.framework.vo.user.UserVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/approve")
public class ApproveController extends ParentController {

    // [검색옵션] 날짜
    @ModelAttribute("searchDateRangeOptionList")
    public List<SelectOptionVO> searchDateRangeOptionList() {
        return getCommonService().getBoardPublicServiceImpl().getSearchDateRangeOptionList("APPROVE");
    }

    // [검색옵션] 키워드
    @ModelAttribute("searchKeywordTypeOptionList")
    public List<SelectOptionVO> searchKeywordTypeOptionList() {
        return getCommonService().getBoardPublicServiceImpl().getSearchKeywordTypeList();
    }

    // [검색옵션] 진행상태
    @ModelAttribute("searchStateTypeOptionList")
    public List<SelectOptionVO> searchStateTypeOptionList() {
        return getCommonService().getApproveService().getSearchStateTypeOptionList();
    }

    /**
     * 결제 발신함 화면 조회
     * @param mv
     * @return
     */
    @GetMapping("/request")
    public ModelAndView findAll(ModelAndView mv) {
        mv.setViewName("board/approve-request-list");

        BoardSearchVO boardSearchVO = new BoardSearchVO();
        boardSearchVO.setBoardCd("MENU0201");
        boardSearchVO.setPaging(Paging.builder()
                .currentPage(1)
                .displayRow(10)
                .build()
        );

        // 전체 게시물 수
        int totalCount = getCommonService().getApproveService().totalCount(boardSearchVO);
        mv.addObject("totalCount", totalCount);

        // 목록 조회
        List<BoardPublicVO> list = getCommonService().getApproveService().findAll(boardSearchVO);
        mv.addObject("list", list);

        // paging
        SearchVO searchVO = new SearchVO();
        searchVO.setPaging(Paging.builder()
                .currentPage(1)
                .displayRow(10)
                .build()
        );
        Paging paging = ObjectHandlingUtil.pagingOperatorBySearch(searchVO, totalCount);
        mv.addObject("paging", paging);

        return mv;
    }

    /**
     * @param mv
     * @param seq
     * @return
     */
    @GetMapping("/{seq}")
    public ModelAndView findById(ModelAndView mv, @PathVariable long seq) {
        mv.setViewName("board/detail");

        BoardSearchVO boardSearchVO = new BoardSearchVO();
        boardSearchVO.setBoardSeq(seq);
        BoardPublicVO rowData = getCommonService().getBoardService().findById(boardSearchVO);
        mv.addObject("rowData", rowData);
        mv.addObject("replyList", rowData.getReplyList());
        List<String> attachIdList = getFileUtil().splitAttachId(rowData.getAttachId());
        List<FilePublicVO> files= getCommonService().getFileServiceImpl().getFiles(attachIdList);

        mv.addObject("files", files);
        getCommonService().getBoardService().increaseHits(seq);

        return mv;
    }

    /**
     * 전자결제 작성 화면
     * @param mv
     * @return
     */
    @GetMapping("/registration")
    public ModelAndView registration(ModelAndView mv, BoardPublic boardPublic) {
        mv.setViewName("approve/registration");
        mv.addObject("boardPublic", boardPublic);

        // 메뉴 목록 조회
        List<CommonCodeVO> list = getCommonService().getAuthorityService().findMenuList(new String());
        mv.addObject("list", list);
//        mv.addObject("code", new CommonCodeVO());
        mv.addObject("users", new ArrayList<UserVO>());

        return mv;
    }

    /**
     * @param mv
     * @param seq
     * @return
     */
    @GetMapping("/edit/{seq}")
    public ModelAndView editForm(ModelAndView mv, @PathVariable long seq) {
        mv.setViewName("board/edit");

        BoardSearchVO boardSearchVO = new BoardSearchVO();
        boardSearchVO.setBoardSeq(seq);
        BoardPublicVO rowData = getCommonService().getBoardService().findById(boardSearchVO);
        mv.addObject("rowData", rowData);
        List<String> attachList = getFileUtil().splitAttachId(rowData.getAttachId());
        List<FilePublicVO> files = getCommonService().getFileServiceImpl().getFiles(attachList);
        mv.addObject("files",files);

        return mv;
    }



    /** API **/
    /**
     * 결제 발신 목록 조회 API
     * @param model
     * @param paramMap
     * @return
     */
    @PostMapping("/api/list")
    public String findAll(Model model, @RequestBody Map<String, Object> paramMap) {
        ObjectMapper om = new ObjectMapper();
        BoardSearchVO boardSearchVO = om.convertValue(paramMap.get("boardPublicVO"), BoardSearchVO.class);
        Paging pagingParam = om.convertValue(paramMap.get("paging"), Paging.class);
        boardSearchVO.setBoardCd("MENU0201");
        boardSearchVO.setPaging(Paging.builder()
                .currentPage(pagingParam.getCurrentPage())
                .displayRow(pagingParam.getDisplayRow())
                .totalCount(pagingParam.getTotalCount())
                .build()
        );

        // 전체 게시물 수
        int totalListCount = getCommonService().getApproveService().totalCount(boardSearchVO);

        model.addAttribute("totalCount", totalListCount);
//
//        // 목록 조회
        List<BoardPublicVO> list = getCommonService().getApproveService().findAll(boardSearchVO);
        model.addAttribute("list", list);

        // paging
        Paging paging = Paging.builder()
                .currentPage(boardSearchVO.getPaging().getCurrentPage())
                .displayRow(boardSearchVO.getPaging().getDisplayRow())
                .totalCount(totalListCount)
                .build();
        model.addAttribute("paging", paging);

        return "board/approve-request-list :: #list_wrapper";
    }

    /**
     * @param model
     * @param boardPublic
     * @return
     */
    @ResponseBody
    @PostMapping("/api/registration")
    public ResponseEntity registration(Model model, @RequestPart(value="data") BoardPublic boardPublic, @RequestPart(value="files",required = false) List<MultipartFile> files) throws Exception {
        Map<String, Object> result = null;//getCommonService().getBoardPublicServiceImpl().registration(boardPublic, files);

        return ResponseEntity.ok(result);
    }

    /**
     * @param model
     * @param board
     * @return
     */
    @ResponseBody
    @PostMapping("/api/update/{seq}")
    public ResponseEntity deleteBoard(Model model, @RequestPart(value="data") Board board, @RequestPart(value="files",required = false) List<MultipartFile> files) throws Exception {
        Map<String, Object> result = getCommonService().getBoardPublicServiceImpl().updateBoard(board,files);

        return ResponseEntity.ok(result);
    }

    /**
     * 공통 게시판 삭제 API
     * @param model
     * @param lastId
     * @param checkedList
     * @return
     */
    @ResponseBody
    @DeleteMapping("/api/delete")
    public ResponseEntity deleteBoard(Model model, @RequestParam(value = "lastId") String lastId, @RequestParam(value = "checkedList[]") List<Integer> checkedList) {
        Map<String, Object> result = getCommonService().getApproveService().deleteBySeq(lastId, checkedList);

        getCommonService().getFileServiceImpl().deleteFiles(lastId, checkedList);
        return ResponseEntity.ok(result);
    }

    /**
     * @param model
     * @param paramMap
     * @return
     */
    @DeleteMapping("/api/reply")
    public String removeReply(Model model, @RequestBody Map<String, Object> paramMap) {
        List<BoardReplyVO> replyList = getCommonService().getBoardPublicServiceImpl().removeBoardReply(paramMap);
        model.addAttribute("replyList", replyList);

        return "board/detail :: #reply_wrapper";
    }

}
