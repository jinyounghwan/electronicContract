package com.samsung.framework.domain.token;

import com.samsung.framework.domain.common.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@EqualsAndHashCode(of = "memberId", callSuper = false)
public class Token extends BaseEntity {

    @NotNull(message = "memberId는 필수 값 입니다.")
    private String memberId;
    @NotNull(message = "액세스 토큰은 필수 값 입니다.")
    private String accessToken;
    @NotNull(message = "리프레시 토큰은 필수 값 입니다.")
    private String refreshToken;
    private String tokenType;

    @Builder
    public Token(String memberId, String accessToken, String refreshToken, String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId) {
        super(tableName, logId1, logId2, logType, logJson, remark, regId);
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = "01";
    }
}
