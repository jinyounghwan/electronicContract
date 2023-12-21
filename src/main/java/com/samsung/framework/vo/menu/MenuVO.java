package com.samsung.framework.vo.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuVO {
    // 메뉴 SEQ
    private Long menuSeq;

    // 부모 메뉴 SEQ
    private Long pMenuSeq;

    //메뉴 유형 01:ADMIN , 02:USER
    private String menuType;

    // 메뉴 ID
    private String menuId;

    // 메뉴 DEPTH
    private Long menuDepth;

    // 메뉴 타이틀
    private String menuTitle;

    // 메뉴 URL
    private String menuUrl;

    // 메뉴 순서
    private Long menuOrd;

    // 노출 여부
    private String displayYn;

    // 사용 여부
    private String useYn;

    // 부모 메뉴 타이틀
    private String pTitle;

    // 공통 코드 값
    private String codeCd;

    // 공통 코드 명
    private String codeNm;
}
