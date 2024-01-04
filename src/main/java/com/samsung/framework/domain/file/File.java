package com.samsung.framework.domain.file;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class File {
    // 파일 SEQ
    private Long fileSeq;

    // 원본 파일명
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
    private int size;

    // 삭제 여부
    private String delYn;

    // 생성자
    private String createdBy;

    // 생성일
    private LocalDateTime createdAt;
}
