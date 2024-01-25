package com.samsung.framework.service.contract.documented;

import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.contract.documented.ContractsCreationMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.excel.ExcelPublicServiceImpl;
import com.samsung.framework.service.excel.ExcelService;
import com.samsung.framework.service.file.FilePublicServiceImpl;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.file.FilePublicVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractsCreationService {
    private ContractsCreationMapper contractsCreationMapper;
    private final ContractTemplateMapper contractTemplateMapper;
    private final AccountMapper accountMapper;
    private final FilePublicServiceImpl fileService;
    private final ExcelPublicServiceImpl excelService;

    /**
     * 계약서 일괄 생성
     * @param multipartFiles
     * @return
     * @throws Exception
     */
    public ResultStatusVO bulkUpload(List<MultipartFile> multipartFiles) throws Exception {
        List<FilePublicVO> fileList = fileService.uploadFile(multipartFiles, "Contract/Excel");
        // IJ TODO: 엑셀 데이터 조회부터...
        excelService.readExcelFile(fileList);

        return null;
    }
}
