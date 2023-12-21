package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestTypeEnum {

    LOGIN("00"), READ("01"), CREATE("02"), UPDATE("03"), DELETE("04"), DOWNLOAD("05");

    private String requestType;
}
