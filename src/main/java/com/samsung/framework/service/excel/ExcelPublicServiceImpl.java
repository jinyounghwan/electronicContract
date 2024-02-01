package com.samsung.framework.service.excel;

import com.samsung.framework.common.enums.ContractTemplateEnum;
import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.common.utils.ExcelUtil;
import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.account.ghr.GhrAccountMapper;
import com.samsung.framework.mapper.file.FileMapper;
import com.samsung.framework.vo.account.ghr.GhtAccountVO;
import com.samsung.framework.vo.contract.ContractExcelVO;
import com.samsung.framework.vo.file.FilePublicVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExcelPublicServiceImpl implements ExcelService {

    private final ExcelUtil excelUtil;
    private final FileUtil fileUtil;
    private final FileMapper fileMapper;
    private final GhrAccountMapper ghrAccountMapper;
    private final AccountMapper accountMapper;
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

    public List<List<ContractExcelVO>> readExcelFile(List<FilePublicVO> fileList) throws Exception {
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
        AtomicInteger rowNum = new AtomicInteger();
        for(List<ContractExcelVO> targetList : list){
            getGhrAccountInfo(targetList);
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

    /**
     * TODO: WAGE_TYPE Setting 필요
     * @param list
     */
    public void getGhrAccountInfo(List<ContractExcelVO> list) throws Exception{
        list.stream().iterator().forEachRemaining(data->{
            if(!accountMapper.existsByEmpNo(StringUtil.getInt(data.getEmpNo()))){
                return;
            }
            GhtAccountVO ghrAccount = ghrAccountMapper.getGhrInfo(Integer.parseInt(data.getEmpNo()));
            data.setName(ghrAccount.getName());
            data.setJobTitleEn(ghrAccount.getJob());
            data.setJobTitleHu(ghrAccount.getJob());
            data.setHireDateHu(DateUtil.convertLocalDateTimeToString(ghrAccount.getHireDate(),"YYYY-MM-dd"));
            data.setHireDateEn(DateUtil.getStrContractDateEn(data.getHireDateHu()));
            data.setContractDateHu(data.getContractDate());
            data.setContractDateEn(DateUtil.getStrContractDateEn(data.getContractDate()));
            data.setWageTypeEn(ghrAccount.getWageType());
            data.setWageTypeHu(ghrAccount.getWageType());
            data.setSalaryNo(String.valueOf(ghrAccount.getSalaryNo()));
        });
    }
}
