package com.samsung.framework.vo.contract;


import com.samsung.framework.common.annotation.ExcelColumn;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractExcelVO {
    @ExcelColumn(headerName = "EmpNo")
    @NotNull(message = "EmpNo 정보가 없습니다.")
    String empNo;

    @ExcelColumn(headerName = "hireDateHu")
    String hireDateHu;
    @ExcelColumn(headerName = "hireDateEn")
    String hireDateEn;
    @ExcelColumn(headerName = "SalaryNo")
    String salaryNo;

    @ExcelColumn(headerName = "SalaryHu")
    @NotNull(message = "SalaryHu 정보가 없습니다.")
    String salaryHu;

    @ExcelColumn(headerName = "SalaryEn")
    @NotNull(message = "SalaryEn 정보가 없습니다.")
    String salaryEn;

    @ExcelColumn(headerName = "TemplateCode")
    @NotNull(message = "TemplateCode 정보가 없습니다.")
    String templateCode;

    @ExcelColumn(headerName = "TemplateSeq")
    @NotNull(message = "TemplateSeq 정보가 없습니다.")
    String templateSeq;

    @ExcelColumn(headerName = "ContractDate")
    @NotNull(message = "ContractDate 정보가 없습니다.")
    String contractDate;

    String jobTitleHu;
    String jobTitleEn;
    String wageTypeEn;
    String wageTypeHu;
    String contractDateHu;
    String contractDateEn;
    String name;

    /*
    *   통과여부
    * */
    boolean validation;
    int rowNum;

}
