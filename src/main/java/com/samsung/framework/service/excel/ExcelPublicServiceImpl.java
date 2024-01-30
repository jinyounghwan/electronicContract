package com.samsung.framework.service.excel;

import com.samsung.framework.common.utils.ExcelUtil;
import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.mapper.file.FileMapper;
import com.samsung.framework.vo.contract.ContractExcelVO;
import com.samsung.framework.vo.file.FilePublicVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExcelPublicServiceImpl implements ExcelService {

    private final ExcelUtil excelUtil;
    private final FileUtil fileUtil;
    private final FileMapper fileMapper;

    @Value("${properties.file.rootDir}")
    private String getRootDir;
    @Value("${properties.file.realDir}")
    private String getRealDir;

    /**
     * 엑셀 파일 실제 directory 생성하기
     * @param tableData
     * @return
     * @throws Exception
     */
    public String createExcelFile(String[][] tableData) throws Exception {
        String path = excelUtil.createExcel(tableData, getRootDir + getRealDir);

        int lastIndex =path.lastIndexOf("/");
        int index = path.indexOf(":");

        String fileName = path.substring(lastIndex+1);
        String filePath = path.substring(index+1);

        Long size = FileUtil.getFileSize(path);
        FilePublicVO file =  FilePublicVO.builder()
                                        .originalName(fileName)
                                        .name(fileName)
                                        .fileNo(1)
                                        .extension(fileUtil.checkFileType(fileName))
                                        .storagePath(filePath)
                                        .size(String.valueOf(size))
                                        .createdBy("hsk9839")
                                        .build();
        int save = saveExcelFile(file);
        return file.getName();
    }

    public List<List<ContractExcelVO>> readExcelFile(List<FilePublicVO> fileList) {
        Iterator<FilePublicVO> iter = fileList.iterator();
        List<List<ContractExcelVO>> list = new ArrayList<>();
        iter.forEachRemaining(value->{
            String filePath = value.getStoragePath() + "/" + value.getName();
            try {
                 list.add(ExcelUtil.readExcel(filePath,value.getName(), ContractExcelVO.class));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        for(List<ContractExcelVO> targetList : list){
            targetList.stream().iterator().forEachRemaining(data->{
                if(data.getTemplateCode().equals("TEMP2000")){
                    if(StringUtil.isEmpty(data.getSalaryHu()) || StringUtil.isEmpty(data.getSalaryHu())) throw new RuntimeException("일괄 업로드를 실패하였습니다.");
                }
            });
        }
        return list;
    }

    /**
     * 엑셀 파일 DB 저장
     * @param file
     * @return
     */
    public int saveExcelFile(FilePublicVO file){
        return fileMapper.saveExcelFile(file);
    }

}
