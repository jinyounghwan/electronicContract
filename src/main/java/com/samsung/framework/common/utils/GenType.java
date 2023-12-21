package com.samsung.framework.common.utils;

import lombok.Getter;

@Getter
public enum GenType {
    // 알파벳 소문자
    LowerCase,
    // 알파벳 대문자
    UpperCase,
    // 숫자
    Number,
    // 소문자, 숫자 조합
    LowerCaseAndNumber,
    // 특수기호
    SpecialMarks
}
