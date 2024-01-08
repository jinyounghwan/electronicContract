package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PositionEnum {
    /**
     * 직위 확정 X (임시 직위 Enum 타입)
     */
    STAFF("POSI","01"),
    SENIOR_MANAGER("POSI", "02"),
    MANAGER("POSI", "03"),
    DEPUTY_MANAGER("POSI","04"),
    GENERAL_MANAGER("POSI","05");


    private String prefix;
    private String parentCode;

    /**
     * 전체 메뉴 코드
     * @param key
     * @return
     */
    public static String menuCode(PositionEnum key) {
        return key.getPrefix() + key.getParentCode();
    }
}
