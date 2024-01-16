package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.domain.contract.SaveContractRequest;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.contract.documented.ContractCreationMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.excel.ExcelPublicServiceImpl;
import com.samsung.framework.service.file.FilePublicServiceImpl;
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
public class ContractCreationService {

    private final ContractTemplateMapper contractTemplateMapper;
    private final AccountMapper accountMapper;
    private final FilePublicServiceImpl fileService;
    private final ExcelPublicServiceImpl excelService;

    /**
     * 계약서 생성
     * @param target
     * @return
     */
    public ResultStatusVO saveContract(SaveContractRequest target) {
        log.info(target.toString());

        // IJ TODO: 템플릿 코드 검증
        if(!isTemplateCodeExists(Integer.parseInt(target.getTemplateCode()))) {
            return ObjectHandlingUtil.setSingleObjResultStatusVO(null, ResultCodeMsgEnum.NO_TEMPLATE_CODE);
        }

        // IJ TODO: 임직원 번호 검증 NOTE:매퍼 파일 변경 가능성 있음.
        if (!accountMapper.existsByEmpNo(target.getEmpNo())) {
            return ObjectHandlingUtil.setSingleObjResultStatusVO(null, ResultCodeMsgEnum.INVALID_EMP_NO);
        }

        // IJ TODO: 계약서 생성

        return new ResultStatusVO();
    }

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

    private boolean isTemplateCodeExists(int templateCode) {
        if (contractTemplateMapper.getContractTemplateCode(templateCode) < 1) {
            return false;
        }
        return true;
    }
}
