package com.samsung.framework.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ExcelUtil {
    public String createExcel(String[][] tableData, String filePath) throws Exception {
        // 워크북 생성
        SXSSFWorkbook workBook = new SXSSFWorkbook();

        Sheet sheet = workBook.createSheet("Sheet1");
        int rowNum = tableData.length;
        int columnNum = tableData[0].length;

        log.info("rowNum : {}", rowNum);
        log.info("columnNum : {} ", columnNum);
        // 행 생성
        Row row = null;

        Cell cell = null;

        for(int i=0; i<rowNum; i++){
            row = sheet.createRow(i);
            for(int j=0; j<columnNum; j++){
                cell = row.createCell(j);
                if( i==0 && StringUtil.isEmpty(tableData)){
                    cell.setCellValue("체크박스");
                } else{
                    cell.setCellValue(tableData[i][j]);
                }

            }
        }
        workBook.setCompressTempFiles(true);
        filePath = FileUtil.getOsRootDir() + filePath + "/" + DateUtil.getUtcNowDateFormat("yyMM");
        FileUtil.makeDirectories(filePath);
        String fileName = FileUtil.createFileName("text.xlsx");
        FileOutputStream fos = new FileOutputStream(filePath+"/"+ fileName);
        workBook.write(fos);
        workBook.close();
        fos.close();

        return filePath+"/"+fileName;
    }

    /**
     * 엑셀 데이터 읽기
     * @param filePath
     */
    public List<String> readExcel(String filePath){
        ArrayList<String> list = new ArrayList<>();
        try{
            File f = new File(filePath);
            FileInputStream fis = new FileInputStream(f);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            String val = "";
            // 엑셀 index는 0부터 시작
            int rowIndex=0;
            int colIndex=0;
            if(workbook != null){
                //시트 수
                XSSFSheet sheet = workbook.getSheetAt(0);

                for(int i= 1; i < sheet.getPhysicalNumberOfRows(); i++){
                    XSSFRow row = sheet.getRow(i);

                    for(int cellIdx=0; cellIdx<row.getPhysicalNumberOfCells(); cellIdx++){
                        XSSFCell cell = row.getCell(cellIdx);
                        log.info("cell data : {}", cell);
                        if(cell != null){
                            switch(cell.getCellType()){
                                case NUMERIC :
                                    val = cell.getNumericCellValue()+"";
                                    list.add(val);
                                    break;
                                case STRING:
                                    val = cell.getStringCellValue()+"";
                                    list.add(val);
                                    break;
                                case BLANK:
                                    val = cell.getBooleanCellValue()+"";
                                    list.add(val);
                                    break;
                                case ERROR:
                                    val = cell.getErrorCellValue()+"";
                                    list.add(val);
                                    break;
                            }
                        }
                    }
                }
                log.info("sheet : {}", sheet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
