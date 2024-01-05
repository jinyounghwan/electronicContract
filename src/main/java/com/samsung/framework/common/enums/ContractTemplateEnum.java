package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContractTemplateEnum {

      EMPLOYEEMENT("TMPL", "1000")
    , SALARY("TMPL", "2000")
    , PRIVACY_AGREEMENT("TMPL", "3000")
    ;

    private String prefix;
    private String parentCode;

    /**
     * 전체 메뉴 코드
     * @param key
     * @return
     */
    public static String menuCode(ContractTemplateEnum key) {
        return key.getPrefix() + key.getParentCode();
    }
}
