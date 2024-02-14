package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MapKeyStringEnum {
    // Common
      RESULT_STATUS("resultStatus")
    , TEST("test")
    , DATA("data")
    , CODE("code")
    , MESSAGE("message")
    , PAGING("paging")
    , ROLES("roles")
    , AUTH("authority")

    // Object
    , MEMBER("member")
    , MEMBER_VO("memberVO")
    , LOGIN_VO("loginVO")
    , TOKEN_VO("tokenVO")
    , TOKEN_OBJECT("tokenObject")
    , BOARD("board")
    , BOARD_VO("boardVO")
    , USER("user")
    , MENU("menu")

    // Collection
    , MEMBER_LIST("memberList")
    , CODE_LIST("codeList")
    , BOARD_LIST("boardList")
    , MENU_LIST("menuList")
    , FILE_LIST("fileList")
    // Token
    , JWT_USERNAME("userName")
    , JWT_ROLES("roles")
    , TOKEN_REISSUE("reissue")

    // Log
    , SAVE_LOG("saveLog")
    ;

    private String keyString;
}
