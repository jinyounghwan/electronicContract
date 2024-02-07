package com.samsung.framework.domain.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@ToString
@Setter
@Getter
@NoArgsConstructor
public class SaveContractRequest {
    @NotNull(message = "계약일은 필수 값 입니다.")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "템플릿 코드는 필수 값 입니다.")
    private String templateCode;

    @NotNull(message = "임직원 번호는 필수 값 입니다.")
    private String empNo;

    private String salaryHu;
    private String salaryEn;
}
