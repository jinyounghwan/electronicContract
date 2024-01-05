package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccountTypeEnum {

      EMPLOYEE("ACCT", "001")
    , ADMIN("ACCT", "002")
    , SYSTEM_OPERATOR("ACCT", "999")
    ;

    private String prefix;
    private String parentCode;

    /**
     * 전체 메뉴 코드
     * @param key
     * @return
     */
    public static String menuCode(AccountTypeEnum key) {
        return key.getPrefix() + key.getParentCode();
    }
}
