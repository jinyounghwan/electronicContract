package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileTypeEnum {
    IMAGE("01"), VIDEO("02"), EXCEL("03"), WORD("04"), HANGEUL("05"), OTHER("06");
    private String fileType;
}
