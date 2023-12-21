package com.samsung.framework.vo.file;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileVO {
    // 파일 SEQ
    private Long fileSeq;
    
    // 테이블 명
    private String entityName;

    // 테이블 SEQ
    private Long entitySeq;

    // 테이블 TYPE
    private String entityType;

    // 파일 경로
    private String filePath;
    
    // 파일 이름
    private String fileName;

    // 기존 파일 명
    private String originFileName;

    // 파일 유형 01:이미지, 02:비디오, 03:엑셀, 04:워드, 05:한글, 06: 그 외 문
    private String fileType;

    // 파일 사이즈
    private Long fileSize;

}
