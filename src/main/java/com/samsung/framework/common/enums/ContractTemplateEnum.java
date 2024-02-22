package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum ContractTemplateEnum {

      EMPLOYMENT("TMPL", "1000", "Employeement contract")
    , SALARY("TMPL", "2000", "Salary contract")
    , PRIVACY_AGREEMENT("TMPL", "3000", "Privacy agreement")
    ;

    private String prefix;
    private String parentCode;
    private String templateTitle;

    /**
     * 템플릿코드
     * @param key
     * @return
     */
    public static String getTemplateCode(ContractTemplateEnum key) {
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
    public static String getTemplateCode(String target) {

        for (ContractTemplateEnum item : ContractTemplateEnum.values()) {
            if ((item.getPrefix() + item.getParentCode()).equals(target)) {
               return item.getPrefix() +  item.getParentCode();
            }
        }

        return new String("No template title");
    }
}
