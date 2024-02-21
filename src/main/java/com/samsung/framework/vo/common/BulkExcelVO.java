package com.samsung.framework.vo.common;


import com.samsung.framework.common.annotation.ExcelColumn;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
public class BulkExcelVO {

    public BulkExcelVO(@NotNull(message = "EmpNo invalid") String empNo , String msg ) {
        this.empNo = empNo;
        this.msg = msg;
    }

    @NotNull(message = "ContractDate invalid")
    String contractDate;

    @NotNull(message = "TemplateType invalid")
    String templateCode;

    @NotNull(message = "TemplateCode invalid")
    String templateSeq;

    @NotNull(message = "EmpNo invalid")
    String empNo;

    @NotNull(message = "SalaryHu invalid")
    String salaryHu;

    @NotNull(message = "SalaryEn invalid")
    String salaryEn;

    String msg;

}
