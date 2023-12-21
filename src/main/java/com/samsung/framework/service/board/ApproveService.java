package com.samsung.framework.service.board;

import com.samsung.framework.vo.board.BoardPublicVO;
import com.samsung.framework.vo.common.SelectOptionVO;
import com.samsung.framework.vo.search.board.BoardSearchVO;

import java.util.List;
import java.util.Map;

public interface ApproveService {

    /**
     * 결제 게시판 전체 게시물 수 조회
     * @param boardSearchVO
     * @return
     */
    int totalCount(BoardSearchVO boardSearchVO);

    /**
     * 결제 게시판 목록 조회
     * @param boardSearchVO
     * @return
     */
    List<BoardPublicVO> findAll(BoardSearchVO boardSearchVO);

    /**
     * 결제 진행상태 옵션 조회
     * @return
     */
    List<SelectOptionVO> getSearchStateTypeOptionList();

    /**
     * 결재 게시물 삭제
     * @param lastId
     * @param tgtList
     * @return
     */
    Map<String, Object> deleteBySeq(String lastId, List<Integer> tgtList);
}
