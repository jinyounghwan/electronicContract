package com.samsung.framework.domain.board;

import com.samsung.framework.domain.common.BaseEntity;
import com.samsung.framework.vo.file.FilePublicVO;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseEntity {
    // 게시판 SEQ
    private Long boardSeq;

    // 게시판 코드
    private String boardCode;
    private String boardCd;

    // 제목
    @NotNull(message="제목은 필수값 입니다.")
    private String title;

    // 내용
    @NotNull(message="내용은 필수값 입니다.")
    private String contents;

    // 상단 고정 여부
    @NotNull(message="상단 고정 여부는 필수값 입니다.")
    private String fixYn;

    // 노출 여부
    @NotNull(message="노출 여부는 필수값 입니다.")
    private String displayYn;

    // 사용 여부
    @NotNull(message="사용 여부는 필수값 입니다.")
    private String useYn;

    // 수정자 ID
    private String lastId;

    // 파일들 (구분자 ,)
    private String attachId;
    // 파일 목록
    private List<FilePublicVO> files;
    @Builder
    public Board(String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId,Long boardSeq,String boardCode, String title, String contents, String fixYn, String displayYn, String useYn, String attachId,List<FilePublicVO> files){
        super(tableName, logId1, logId2, logType, logJson, remark, regId);
        this.boardSeq = boardSeq;
        this.boardCode = boardCode;
        this.title = title;
        this.contents = contents;
        this.fixYn = fixYn;
        this.displayYn = displayYn;
        this.useYn = useYn;
        this.attachId = attachId;
        this.files = files;
    }
}
