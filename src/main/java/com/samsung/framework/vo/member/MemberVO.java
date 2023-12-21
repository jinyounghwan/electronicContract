package com.samsung.framework.vo.member;

import com.samsung.framework.domain.common.BaseEntity;
import lombok.*;

import java.util.Date;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MemberVO extends BaseEntity {
    private int userSeq;
    private String userId;
    private String userPwd;
    private String userType;
    private String userNm;
    private String userTel;
    private String userEmail;
    private String authority; // 권한 임시로 String으로 변경
    private String deptCd;
    private String appointCode;
    private String positionCode;
    private String userEtc;
    private String authEmailSendTime;
    private Date regDt;
    @Builder
    public MemberVO(String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId, int userSeq, String userType, String userId, String userPwd, String userNm, String userEmail, String authority, String deptCd, String appointCode, String positionCode, String userTel, String userEtc, Date regDt, String authEmailSendTime) {
        super(tableName, logId1, logId2, logType, logJson, remark, regId);
        this.userSeq = userSeq;
        this.userId = userId;
        this.userPwd = userPwd;
        this.userType = userType;
        this.userNm = userNm;
        this.userTel = userTel;
        this.userEmail = userEmail;
        this.authority = authority;
        this.deptCd = deptCd;
        this.appointCode = appointCode;
        this.positionCode = positionCode;
        this.userEtc = userEtc;
        this.regDt = regDt;
        this.authEmailSendTime = authEmailSendTime;
    }
}
