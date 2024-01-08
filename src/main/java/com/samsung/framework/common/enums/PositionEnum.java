package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PositionEnum {
    /**
     * 직위 확정 X (임시 직위 Enum 타입)
     */
    STAFF("POSI01","사원"),
    SENIOR_MANAGER("POSI02", "대리"),
    MANAGER("POSI03", "과장"),
    DEPUTY_MANAGER("POSI04","차장"),
    GENERAL_MANAGER("POSI05","부장");


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
