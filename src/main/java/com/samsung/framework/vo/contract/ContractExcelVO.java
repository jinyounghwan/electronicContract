package com.samsung.framework.vo.contract;


import com.samsung.framework.common.annotation.ExcelColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractExcelVO {
    @ExcelColumn(headerName = "EmpNo")
    String empNo;
    @ExcelColumn(headerName = "hireDateHu")
    String hireDateHu;
    @ExcelColumn(headerName = "hireDateEn")
    String hireDateEn;
    @ExcelColumn(headerName = "SalaryNo")
    String salaryNo;
    @ExcelColumn(headerName = "SalaryHu")
    String salaryHu;
    @ExcelColumn(headerName = "SalaryEn")
    String salaryEn;
    @ExcelColumn(headerName = "TemplateCode")
    String templateCode;
    @ExcelColumn(headerName = "TemplateSeq")
    String templateSeq;
    @ExcelColumn(headerName = "ContractDate")
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
