package com.samsung.framework.domain.contract;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProgressRequest {
    @NotNull(message = "contract number 는(은) 필수 값 입니다.")
    private int contractNo;
    @Setter
    private String docStatus;


}
