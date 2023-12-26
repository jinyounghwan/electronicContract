package com.samsung.framework.domain.file;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {
    // 파일 SEQ
    private  Long fileSeq;

    // 저장한 곳 식별
    private String targetId;

    // 파일 용량
    private String fileSize;

    //파일 번호
    private Long fileNo;

    // 파일 경로
    private String filePath;

    // 파일 이름
    private String fileNm;

    // 기존 파일 명
    private String fileNmOrg;

    // 삭제 여부
    private String delYn;

    // 파일 유형 01:이미지, 02:비디오, 03:엑셀, 04:워드, 05:한글, 06: 그 외 문
    private String fileType;

    // 등록일
    private String regDt;

    // 등록자
    private String regId;

    // 마지막 등록일
    private String lastId;
}
