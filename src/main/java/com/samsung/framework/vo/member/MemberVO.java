package com.samsung.framework.vo.member;

import com.samsung.framework.domain.common.BaseEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MemberVO extends BaseEntity {
    private int empNo;
    private String deptCode;
    private String userId;
    private String userPw;
    private String name;
    private String accountType;
    private String position;
    private String email; // 권한 임시로 String으로 변경
    private String phone;
    private String useYn;
    private LocalDateTime employedAt;
    private LocalDateTime resignedAt;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    @Builder
    public MemberVO(String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId, int empNo, String deptCode, String userId, String userPw, String name, String accountType, String position, String email, String phone, String useYn, LocalDateTime employedAt, LocalDateTime resignedAt, String createdBy, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt) {
        super(tableName, logId1, logId2, logType, logJson, remark, regId);
        this.empNo = empNo;
        this.deptCode = deptCode;
        this.userId = userId;
        this.userPw = userPw;
        this.name = name;
        this.accountType = accountType;
        this.position = position;
        this.email = email;
        this.phone = phone;
        this.useYn = useYn;
        this.employedAt = employedAt;
        this.resignedAt = resignedAt;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }
}
