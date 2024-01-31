package com.samsung.framework.vo.contract;


import com.samsung.framework.common.annotation.ExcelColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractExcelVO {
    @ExcelColumn(headerName = "name")
    String name;
    @ExcelColumn(headerName = "empNo")
    String empNo;
    @ExcelColumn(headerName = "hireDateHu")
    String hireDateHu;
    @ExcelColumn(headerName = "hireDateEn")
    String hireDateEn;
    @ExcelColumn(headerName = "jobTitleHu")
    String jobTitleHu;
    @ExcelColumn(headerName = "jobTitleEn")
    String jobTitleEn;
    @ExcelColumn(headerName = "salaryNo")
    String salaryNo;
    @ExcelColumn(headerName = "salaryHu")
    String salaryHu;
    @ExcelColumn(headerName = "salaryEn")
    String salaryEn;
    @ExcelColumn(headerName = "wageTypeEn")
    String wageTypeEn;
    @ExcelColumn(headerName = "wageTypeHu")
    String wageTypeHu;
    @ExcelColumn(headerName = "contractDateHu")
    String contractDateHu;
    @ExcelColumn(headerName = "templateCode")
    String templateCode;
    @ExcelColumn(headerName = "contractDateEn")
    String contractDateEn;
    @ExcelColumn(headerName = "templateSeq")
    String templateSeq;
    /*
    *   통과여부
    * */
    boolean validation;
    int rowNum;

}
