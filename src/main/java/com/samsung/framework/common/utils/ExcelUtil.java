package com.samsung.framework.common.utils;


import com.samsung.framework.common.annotation.ExcelColumn;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static <T> List<T> readExcel(String filePath, String filename, Class<T> voClass) throws FileNotFoundException {
       Workbook workbook = getWorkbook(filePath, filename);
       List<T> voList = new ArrayList<>();
       workbook.sheetIterator().forEachRemaining(sheet -> {
           int firstRowNum = sheet.getFirstRowNum();
           Row firstRow = sheet.getRow(firstRowNum);
           Map<String, Integer> finder = createFinder(firstRow, voClass);

           sheet.rowIterator().forEachRemaining(row-> {
               if(row.getRowNum() != firstRowNum) {
                   T t = rowToVO(finder, row, voClass);
                   if(t!= null) {
                       voList.add(t);
                   }
               }
           });
       });
       return voList;
    }

    private static Workbook getWorkbook(String filePath, String fileName) throws FileNotFoundException {
         String extension = FileUtil.getFileExtension(fileName);
         File f = new File(filePath);
         FileInputStream fis = new FileInputStream(f);
         try{
             if(extension.equals(".xlsx"))
                 return new XSSFWorkbook(fis);
             else if(extension.equals(".xls")){
                 return new HSSFWorkbook(new FileInputStream(f));
             }
         } catch(Exception e) {
             e.printStackTrace();
         }
         throw new RuntimeException("엑셀 파일이 아닙니다.");
    }

    private static <T> Map<String,Integer> createFinder(Row row, Class<T> voClass){
        Map<String, Integer> finder = new HashMap<>();
        Field[] fields = voClass.getDeclaredFields();
        for(Field field : fields){
            String name;
            try{
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                name = annotation.headerName();
            } catch(Exception e){
                name = field.getName();
            }
            finder.put(name, -1);
        }

        row.cellIterator().forEachRemaining(cell-> {
            if(cell.getCellType() != CellType.STRING) throw new RuntimeException("구분 타입은 문자열 이여야 합니다.");
            finder.put(cell.getStringCellValue(), cell.getColumnIndex());
        });

        return finder;
    }
    private static<T> T rowToVO(Map<String, Integer> finder, Row row, Class<T> voClass){
        T t;
        try{
            Constructor<T> noArgsConstructor = voClass.getConstructor(null);
            t = noArgsConstructor.newInstance();
            Field[] fields = voClass.getDeclaredFields();
            for(Field field: fields){
                log.info("field : {}", field);

                String name;
                try{
                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    name = annotation.headerName();
                } catch (Exception e){
                    name = field.getName();
                }
                log.info("name :: {}", name);
                Integer index = finder.get(name);

                if(index == -1) continue;
                Cell cell = row.getCell(index);
                field.setAccessible(true);
                field.set(t, matchFields(field, cell));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    private static Object matchFields(Field field, Cell cell){
        Object result = null;
        switch(cell.getCellType()) {
            case _NONE:
            case BLANK:
            case ERROR:
                break;
            case STRING:
            case FORMULA:
                result = cell.getStringCellValue();
                break;
            case BOOLEAN:
                result = cell.getBooleanCellValue();
                break;
            case NUMERIC:
                if(field.getType().getSimpleName().equals("Long")){
                    result = Double.valueOf(cell.getNumericCellValue()).longValue();
                } else{
                    result = (int)cell.getNumericCellValue();
                }
                break;
        }
        log.info("result :: {}", result);
        return result;
    }
}
