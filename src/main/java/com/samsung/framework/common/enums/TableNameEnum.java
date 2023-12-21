package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TableNameEnum {

      CODE("공통 코드")
    , MEMBER("회원")
    , BOARD("게시판")
    , AUTH("권한")
    , ENTITY_LOG("테이블 처리 이력")
    , ENTITY_HISTORY("테이블 처리 history")
    , MENU("메뉴 관리")
    , MENU_LOG("메뉴 접근 이력")
    , TOKEN("토큰")
    , LOGIN("로그인 관리")
    ;

    private String desc;
}
