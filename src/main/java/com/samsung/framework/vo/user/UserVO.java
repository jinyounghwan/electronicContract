package com.samsung.framework.vo.user;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode(of = {"empNo","userId","name"}, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserVO {
    private int empNo;
    private String deptCode;
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
    private String hireDateEn;
    private String hireDateHu;
    private String salaryNo;
    private String jobTitle;
    private String wageType;
    private String wageTypeEn;
    private String wageTypeHu;

}
