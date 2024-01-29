package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LogTypeEnum {

    LOGIN("로그인"), CONTRACT_PROCESS("계약서 진행상황"), COMPLETION("계약서 완료");

    private String description;

}
