package com.samsung.framework.domain.file;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {
    // 파일 SEQ
    private  Long fileSeq;

    // 게시판 SEQ
    private String boardSeq;

    // 파일 크기
    private Long fileSize;

    //파일 번호
    private Long fileNo;

    // 파일 경로
    private String filePath;

    // 파일 이름
    private String fileNm;

    // 기존 파일 명
    private String fileNmOrg;

    // 썸네일 여부
    private String thumbnailYn;

    // 삭제 여부
    private String delYn;

    // 등록일
    private String regDt;

    // 등록자
    private String regId;
}
