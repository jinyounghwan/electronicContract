package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HttpRequestTypeEnum {

      GET("조회")
    , POST("입력")
    , PATCH("수정")
    , PUT("수정")
    , DELETE("삭제")
    , EMPTY("")
    ;

    private String desc;
}
