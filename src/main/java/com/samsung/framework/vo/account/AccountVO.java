package com.samsung.framework.vo.account;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AccountVO {
    private int empNo;
    private String deptCode;
    private String userId;
    private String adminId;
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
    public AccountVO(int empNo, String deptCode, String userId, String adminId, String userPw, String name, String accountType, String position, String email, String phone, String useYn, LocalDateTime employedAt, LocalDateTime resignedAt, String createdBy, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt) {
        this.empNo = empNo;
        this.deptCode = deptCode;
        this.userId = userId;
        this.adminId = adminId;
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
