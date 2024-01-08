package com.samsung.framework.vo.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuVO {
    // 메뉴 SEQ
    private Long menuSeq;

    // 메뉴 코드
    private String menuCode;

    // 메뉴 명
    private String name;

    // 메뉴 순서
    private int ord;

    // 메뉴 URL 정보
    private String path;

    // 노출 여부
    private String displayYn;

    // 사용 여부
    private String useYn;

    // 삭제 여부
    private String delYn;

    // 생성자
    private String createdBy;

    // 생성 일
    private LocalDateTime createdAt;

    // 수정자
    private String updatedBy;

    // 수정일
    private LocalDateTime updatedAt;

     //부모 메뉴 코드
     private String pMenuCode;
}
