package com.samsung.framework.vo.file;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FilePublicVO {
    // 파일 SEQ
    private Long fileSeq;
    
    // 원본 파일 명 
    private String originalName;
    
    // 파일 명 
    private String name;
    
    // 파일 번호
    private int fileNo;
    
    // 확장자
    private String extension;
    
    // 저장 경로
    private String storagePath;
    
    // 파일 크기
    private String size;

    // 삭제 여부
    private String delYn;

    // 생성자
    private String createdBy;

    // 생성일
    private LocalDateTime createdAt;

    // 파일 리스트
    private List<String> attachList;
}
