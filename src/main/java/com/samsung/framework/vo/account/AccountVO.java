package com.samsung.framework.vo.account;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class AccountVO {
    private String empNo;
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
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$"
            , message="비밀번호는 영어와 숫자 포함해서 8~16자리 이내로 입력해주세요.")
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String employedAtStr;
    private String resignedAtStr;
    private LocalDateTime passwordAt;
    private String passwordAtStr;
}
