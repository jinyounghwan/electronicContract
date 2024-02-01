package com.samsung.framework.vo.common;


import com.samsung.framework.common.annotation.ExcelColumn;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BulkExcelVO {

    public BulkExcelVO(@NotNull(message = "EmpNo 정보가 없습니다.") String empNo) {
        this.empNo = empNo;
    }

    @NotNull(message = "ContractDate 정보가 없습니다.")
    String contractDate;

    @NotNull(message = "TemplateCode 정보가 없습니다.")
    String templateCode;

    @NotNull(message = "TemplateSeq 정보가 없습니다.")
    String templateSeq;

    @NotNull(message = "EmpNo 정보가 없습니다.")
    String empNo;

    @NotNull(message = "SalaryHu 정보가 없습니다.")
    String salaryHu;

    @NotNull(message = "SalaryEn 정보가 없습니다.")
    String salaryEn;

}
