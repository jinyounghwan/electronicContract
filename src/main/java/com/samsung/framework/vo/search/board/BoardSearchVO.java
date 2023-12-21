package com.samsung.framework.vo.search.board;

import com.samsung.framework.vo.search.SearchVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearchVO extends SearchVO{ 
    //게시판 SEQ
    private Long boardSeq;
    //게시판 boardCode
    private String boardCd;
    //제목
    private String title;
    //내용
    private String contents;
    //상단 고정 여부
    private String fixYn;
    // 노출 여부
    private String displayYn;
    // 테이블 이름
    private String entityName;
    // 조회 수
    private String hits;
    // 삭제 여부
    private String delYn;
    // 검색 날짜 유형
    private String searchDateType;
    // 검색 키워드 유형
    private String searchKeywordType;
    // [결제] 진행 상태
    private String approveState;
}
