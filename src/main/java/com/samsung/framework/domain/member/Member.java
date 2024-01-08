package com.samsung.framework.domain.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString(callSuper = false)
@Getter
@EqualsAndHashCode(of = {"memberId", "memberName"})
public class Member {

    @Min(message = "아이디는 0보다 큰 수여야 합니다.", value = 0)
    private int empNo;
    private String deptCode;
    @NotNull(message = "멤버 아이디는 필수값 입니다.")
    private String userId;
    private String userPw;
    private String name;
    private String accountType;
    private String position;
    private String email;
    private String phone;
    private String useYn;
    private LocalDateTime employeedAt;
    private LocalDateTime resignedAt;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    @Builder
    public Member(int empNo, String deptCode, String userId, String userPw, String name, String accountType, String position, String email, String phone, String useYn, LocalDateTime employeedAt, LocalDateTime resignedAt, String createdBy, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt) {
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
        this.employeedAt = employeedAt;
        this.resignedAt = resignedAt;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }
}
