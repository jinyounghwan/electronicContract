package com.samsung.framework.domain.board;

import com.samsung.framework.vo.file.FilePublicVO;
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

    // 첨부 파일 ID들
    private String attachId;
    // 결재자 목록
    private List<Long> approverList;

    // 참조자 목록
    private List<Long> referrerList;

    // 첨부 파일 목록
    private List<FilePublicVO> files;
    @Builder
    public BoardPublic(Long boardSeq, String boardCd, @NotNull(message = "제목은 필수값 입니다.") String title, String contents, String hits, String delYn, String regId, String lastId, String attachId,List<Long> approverList, List<Long> referrerList, List<FilePublicVO> files) {
        this.boardSeq = boardSeq;
        this.boardCd = boardCd;
        this.title = title;
        this.contents = contents;
        this.hits = hits;
        this.delYn = delYn;
        this.regId = regId;
        this.lastId = lastId;
        this.attachId = attachId;
        this.approverList = approverList;
        this.referrerList = referrerList;
        this.files = files;
    }
}
