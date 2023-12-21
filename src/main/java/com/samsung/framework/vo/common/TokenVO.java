package com.samsung.framework.vo.common;

import com.samsung.framework.common.enums.AuthorityEnum;
import com.samsung.framework.domain.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@ToString
@Getter
@NoArgsConstructor
public class TokenVO extends BaseEntity {
    private String sub;
    private String userName;
    private String iss;
    private long iat;
    private long exp;
    private Set<AuthorityEnum> authority;

    public TokenVO(String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId) {
        super(tableName, logId1, logId2, logType, logJson, remark, regId);
    }

    @Builder
    public TokenVO(String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId, String sub, String userName, String iss, long iat, long exp, Set<AuthorityEnum> authority) {
        super(tableName, logId1, logId2, logType, logJson, remark, regId);
        this.sub = sub;
        this.userName = userName;
        this.iss = iss;
        this.iat = iat;
        this.exp = exp;
        this.authority = authority;
    }
}
