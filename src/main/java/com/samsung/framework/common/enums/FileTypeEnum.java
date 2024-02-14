package com.samsung.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileTypeEnum {
    IMAGE("01", null), VIDEO("02", null), EXCEL("03", "98"), WORD("04", null), HANGEUL("05", null), OTHER("06", null);
    private String fileType;
    private String templateDownloadFileSeq;
}
