package com.samsung.framework.service.excel;

import com.samsung.framework.common.utils.ExcelUtil;
import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.mapper.file.FileMapper;
import com.samsung.framework.vo.contract.ContractExcelVO;
import com.samsung.framework.vo.file.FilePublicVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public void readExcelFile(List<FilePublicVO> fileList) {
        Iterator<FilePublicVO> iter = fileList.iterator();
        iter.forEachRemaining(value->{
            String filePath = value.getStoragePath() + "/" + value.getName();
            try {
                List<ContractExcelVO> list = ExcelUtil.readExcel(filePath,value.getName(), ContractExcelVO.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

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
