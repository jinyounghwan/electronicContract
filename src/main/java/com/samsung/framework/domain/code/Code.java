package com.samsung.framework.domain.code;

import com.samsung.framework.domain.common.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString(callSuper = false)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Code extends BaseEntity {
    @NotNull(message = "코드는 필수값 입니다.")
    private String code;
    @NotNull(message = "코드명은 필수값 입니다.")
    private String codeName;
    private String remark1;
    private Integer value1;
    private String remark2;
    private Integer value2;
    @NotNull(message = "코드 순서는 필수값 입니다.")
    private Integer codeOrd;
    @NotNull(message = "사용여부는 필수값 입니다.")
    private String useYn;

    @Builder
    public Code(String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId, String code, String codeName, String remark1, Integer value1, String remark2, Integer value2, Integer codeOrd, String useYn) {
        super(tableName, logId1, logId2, logType, logJson, remark, regId);
        this.code = code;
        this.codeName = codeName;
        this.remark1 = remark1;
        this.value1 = value1;
        this.remark2 = remark2;
        this.value2 = value2;
        this.codeOrd = codeOrd;
        this.useYn = useYn;
    }
}
