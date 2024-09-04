package com.samsung.framework.vo.common;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class BulkExcelBasicVO {
    public BulkExcelBasicVO(@NotNull(message = "EmpNo 정보가 없습니다.")String empNo){
        this.empNo = empNo;
    }
    @NotNull(message = "ContractDate invalid.")
    String contractDate;

//    @NotNull(message = "TemplateType invalid.")
//    String templateCode;

    @NotNull(message = "TemplateCode invalid.")
    String templateSeq;

    @NotNull(message = "EmpNo invalid.")
    String empNo;
}
