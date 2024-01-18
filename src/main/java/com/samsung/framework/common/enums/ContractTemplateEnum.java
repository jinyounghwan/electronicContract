package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContractTemplateEnum {

      EMPLOYEEMENT("TMPL", "1000", "Employeement contract")
    , SALARY("TMPL", "2000", "Salary contract")
    , PRIVACY_AGREEMENT("TMPL", "3000", "Privacy agreement")
    ;

    private String prefix;
    private String parentCode;
    private String templateTitle;

    /**
     * 전체 메뉴 코드
     * @param key
     * @return
     */
    public static String menuCode(ContractTemplateEnum key) {
        return key.getPrefix() + key.getParentCode();
    }

    public static String getTemplateTitle(String target) {

        for (ContractTemplateEnum item : ContractTemplateEnum.values()) {
            if ((item.getPrefix() + item.getParentCode()).equals(target)) {
               return item.getTemplateTitle();
            }
        }

        return new String("No template title");
    }
}
