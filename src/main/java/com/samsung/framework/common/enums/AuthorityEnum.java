package com.samsung.framework.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum AuthorityEnum {
    // 권한 코드 USER Type으로 변경
    NO_AUTHORITY("USER0000"), DEVELOPMENT("USER0101"), BUSINESS_SUPPORT("USER0102");

    private String deptCode;

    AuthorityEnum(String deptCode) {
        this.deptCode = deptCode;
    }

    @JsonCreator
    public static AuthorityEnum from(String value) {
        for(AuthorityEnum auth : AuthorityEnum.values()) {
            if(auth.getDeptCode().equals(value)) {
                return auth;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return deptCode;
    }
}
