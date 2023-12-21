package com.samsung.framework.domain.common;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class TokenObjectVO extends BaseEntity {

    private String key;
    private String accessToken;
    private String refreshToken;

    public TokenObjectVO() {
        super(null, null, null, null, null, null, null);
    }

    @Builder
    public TokenObjectVO(String key, String accessToken, String refreshToken, String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId) {
        super(tableName, logId1, logId2, logType, logJson, remark, regId);
        this.key = key;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
