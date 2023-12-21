package com.samsung.framework.domain.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString(callSuper = false)
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
public class SignUpRequest {

    private long seq;
    @NotNull(message = "아이디는 필수 값 입니다.")
    private String id;
    @NotNull(message = "비밀번호는 필수 값 입니다.")
    private String password;
    @NotNull(message = "이름은 필수 값 입니다.")
    private String name;
    private String userTel;
    private String email;
    private String picture;
    private String userType;
    @NotNull(message = "권한은 필수 값 입니다.")
    private String authority; // 권한 임시로 String으로 변경
    private String deptCode;
    private String appointCode;
    private String positionCode;

    @Builder
    public SignUpRequest(long seq, String id, String password, String name, String email, String picture, String userType, String authority, String deptCode, String appointCode, String positionCode, String userTel) {
        this.seq = seq;
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.userTel = userTel;
        this.picture = picture;
        this.userType = userType;
        this.authority = authority;
        this.deptCode = deptCode;
        this.appointCode = appointCode;
        this.positionCode = positionCode;
    }
}
