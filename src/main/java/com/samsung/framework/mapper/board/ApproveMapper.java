package com.samsung.framework.mapper.board;

import com.samsung.framework.domain.board.Board;
import com.samsung.framework.domain.board.BoardPublic;
import com.samsung.framework.vo.board.BoardPublicVO;
import com.samsung.framework.vo.board.BoardReplyVO;
import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Mapper
public interface ApproveMapper {
    /**
     * 공통 게시판 전체 게시물 수
     * @param searchVO
     * @return int {@link Integer}
     */
    int findAllTotalCount(SearchVO searchVO);

    /**
     * 공통 게시판 목록 조회
     * @return
     */
    List<BoardPublicVO> findAll(SearchVO searchVO);

    /**
     * 공통 게시판 단건 조회
     * @param search {@link SearchVO}
     * @return
     */
    BoardPublicVO findById(SearchVO search);

    /**
     * 공통 게시판 삭제
     * @param list
     * @return
     */
    int deleteList(Map<String, Object> list);

    /**
     * 공통 게시판 수정
     * @param board {@link Board}
     * @return {@link Integer}
     */
    int updateBoard(Board board, List<MultipartFile> files);

    /**
     * 공통 게시판 등록
     * @param boardPublic {@link BoardPublic}
     * @return {@link Integer}
     */
    int registrationBoard(BoardPublic boardPublic);

    /**
     * 조회수 증가
     * @param boardSeq {@link Long}
     * @return {@link Integer}
     */
    int increaseHits(long boardSeq);

    /**
     * 게시판 댓글 목록
     * @param boardSeq {@link Long}
     * @return {@link List}
     */
    List<BoardReplyVO> findReplyBySeq(long boardSeq);

    /**
     * 게시판 댓글 삭제
     * @param paramMap {@link Map}
     * @return {@link Map}
     */
    int removeReplyBySeq(Map<String, Object> paramMap);
}
