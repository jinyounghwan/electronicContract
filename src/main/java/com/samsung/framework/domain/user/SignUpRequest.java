package com.samsung.framework.domain.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@ToString(callSuper = false)
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"empNo", "name"})
public class SignUpRequest {
    @NotNull(message= "사번은 필수 값 입니다.")
    private long empNo;
    private String deptCode;
    private String userId;
    @NotNull(message = "비밀번호는 필수 값 입니다.")
    private String userPw;
    @NotNull(message = "이름은 필수 값 입니다.")
    private String name;
    private String accountType;
    private String position;
    private String email;
    private String phone;
    private String useYn;
    private LocalDateTime employedAt;
    private LocalDateTime resignedAt;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;


    @Builder
    public SignUpRequest(int empNo, String deptCode, String userId, String userPw, String name, String accountType, String position, String email, String phone,String useYn
            , LocalDateTime employedAt,  LocalDateTime resignedAt, String createdBy, LocalDateTime createdAt, String updatedBy, LocalDateTime updatedAt) {
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
