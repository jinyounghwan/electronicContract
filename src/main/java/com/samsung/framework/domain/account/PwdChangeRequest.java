package com.samsung.framework.domain.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PwdChangeRequest {
    @NotNull(message = "password는 필수 값 입니다.")
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$"
            , message="비밀번호는 영어와 숫자 포함해서 8~16자리 이내로 입력해주세요.")
    private String password;
}
