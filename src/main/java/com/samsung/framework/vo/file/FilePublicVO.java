package com.samsung.framework.vo.file;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilePublicVO {
    // 파일 SEQ
    private Long fileSeq;

    // 게시판 식별 번호
    private Long boardSeq;

    //파일 번호
    private Long fileNo;

    // 파일 이름
    private String fileNm;

    // 파일 원본 이름
    private String fileNmOrg;

    // 파일 용량
    private String fileSize;

    // 파일 경로
    private String filePath;

    // 썸네일 여부
    private String thumbnailYn;

    // 삭제 여부
    private String delYn;

    // 등록일
    private String regDt;

    // 등록자
    private String regId;

    // 마지막 등록일
    private String lastId;
}
