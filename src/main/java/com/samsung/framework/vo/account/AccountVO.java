package com.samsung.framework.vo.account;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDateTime lastLogin;
    private String updatedAtStr;
    private String createdAtStr;
    private String lastLoginStr;

}
