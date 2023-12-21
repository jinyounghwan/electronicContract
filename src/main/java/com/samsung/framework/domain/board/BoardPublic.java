package com.samsung.framework.domain.board;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardPublic {
    // 게시판 SEQ
    private Long boardSeq;

    // 게시판 코드
    @NotNull(message = "게시판 코드는 필수값 입니다.")
    private String boardCd;

    // 제목
    @NotNull(message="제목은 필수값 입니다.")
    private String title;

    // 내용
    private String contents;

    // 조회수
    private String hits;

    // 삭제 여부
    private String delYn;

    // 등록자 ID
    private String regId;

    // 수정자 ID
    private String lastId;

    // 결재자 목록
    private List<Long> approverList;

    // 참조자 목록
    private List<Long> referrerList;

    @Builder
    public BoardPublic(Long boardSeq, String boardCd, @NotNull(message = "제목은 필수값 입니다.") String title, String contents, String hits, String delYn, String regId, String lastId, List<Long> approverList, List<Long> referrerList) {
        this.boardSeq = boardSeq;
        this.boardCd = boardCd;
        this.title = title;
        this.contents = contents;
        this.hits = hits;
        this.delYn = delYn;
        this.regId = regId;
        this.lastId = lastId;
        this.approverList = approverList;
        this.referrerList = referrerList;
    }
}
