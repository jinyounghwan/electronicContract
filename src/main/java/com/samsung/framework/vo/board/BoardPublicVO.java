package com.samsung.framework.vo.board;

import com.samsung.framework.vo.file.FilePublicVO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@EqualsAndHashCode(of = {"boardSeq", "boardCd"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BoardPublicVO {
    private long boardSeq;
    private String boardCd;
    private String title;
    private String contents;
    private int hits;
    private String delYn;
    private LocalDateTime regDt;
    private String regDtStr;
    private String regId;
    private LocalDateTime lastDt;
    private String lastDtStr;
    private String lastId;
    private int rowNum;
    // 결재 완료일
    private String completeDtStr;
    // 진행상태
    private String approveState;
    private String approveStateStr;

    // Reply
    private List<BoardReplyVO> replyList;

    // 파일 목록
    private List<FilePublicVO> files;
    public void setApproveStateInKorean() {
        if(this.approveState.equalsIgnoreCase("HDLE0401")) {
            setApproveStateStr("요청");
        }else if(this.approveState.equalsIgnoreCase("HDLE0402")) {
            setApproveStateStr("대기");
        }else if(this.approveState.equalsIgnoreCase("HDLE0403")) {
            setApproveStateStr("진행중");
        }else if(this.approveState.equalsIgnoreCase("HDLE0404")) {
            setApproveStateStr("완료");
        }else if(this.approveState.equalsIgnoreCase("HDLE0405")) {
            setApproveStateStr("승인");
        }else if(this.approveState.equalsIgnoreCase("HDLE0406")) {
            setApproveStateStr("반려");

        }
    }
}
