package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PositionEnum {
    /**
     * 직위 확정 X (임시 직위 Enum 타입)
     */
    VICE_PRESIDENT("POSI","01"),
    TECHNICIAN("POSI","02"),
    STAFF_ENGINEER("POSI", "03"),
    STAFF("POSI", "04"),
    SENIOR_TECHNICIAN("POSI","05"),
    SENIOR_STAFF_ENGINEER("POSI","06"),
    SENIOR_STAFF("POSI","07"),
    SENIOR_OPERATOR("POSI","08"),
    SENIOR_MANAGER("POSI","09"),
    SENIOR_ENGINEER("POSI","10"),
    PRICIPAL_ENGINEER("POSI","11"),
    OPERATOR("POSI","12"),
    MANAGER("POSI","13"),
    JUNIOR_STAFF("POSI","14"),
    INTERPRETER("POSI","15"),
    ENGINEER("POSI","16"),
    DIRECTOR("POSI", "17"),
    ASSISTANT_MANAGER("POSI","18")
    ;


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
