package com.samsung.framework.service.excel;

import com.samsung.framework.common.exception.CustomFileException;
import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.service.common.ParentService;
import com.samsung.framework.vo.file.FilePublicVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;
import java.util.List;

@Component
@Slf4j
public class ExcelPublicServiceImpl extends ParentService implements ExcelService {
    /**
     * 엑셀 파일 실제 directory 생성하기
     * @param tableData
     * @return
     * @throws Exception
     */
    public String createExcelFile(String[][] tableData) throws Exception {
        String path = getExcelUtil().createExcel(tableData, getPropertiesUtil().getFile().getRootDir() + getPropertiesUtil().getFile().getRealDir());

        int lastIndex =path.lastIndexOf("/");
        int index = path.indexOf(":");

        String fileName = path.substring(lastIndex+1);
        String filePath = path.substring(index+1);

        Long size = FileUtil.getFileSize(path);
        FilePublicVO file =  FilePublicVO.builder()
                                        .originalName(fileName)
                                        .name(fileName)
                                        .fileNo(1)
                                        .extension(getFileUtil().checkFileType(fileName))
                                        .storagePath(filePath)
                                        .size(String.valueOf(size))
                                        .createdBy("hsk9839")
                                        .build();
        int save = saveExcelFile(file);
        return file.getName();
    }

    public void readExcelFile(List<MultipartFile> multipartFiles){
        Iterator<MultipartFile> iter = multipartFiles.listIterator();
        iter.forEachRemaining(value->{
            String filePath = "C:\\files\\dejay_public\\" + value.getOriginalFilename();
            getExcelUtil().readExcel(filePath);
        });
    }

    /**
     * 엑셀 파일 DB 저장
     * @param file
     * @return
     */
    public int saveExcelFile(FilePublicVO file){
        return getCommonMapper().getFileMapper().saveExcelFile(file);
    }

}
