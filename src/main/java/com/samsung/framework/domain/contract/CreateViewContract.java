package com.samsung.framework.domain.contract;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CreateViewContract {

    @NotNull(message = "templateSeq 는(은) 필수 값 입니다.")
    private String templateSeq;

    private String employeeId;

    private String salaryHu;

    private String salaryEn;

    private String date;
}
