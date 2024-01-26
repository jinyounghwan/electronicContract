package com.samsung.framework.service.contract.documented;


import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.domain.account.ghr.GhrAccount;
import com.samsung.framework.mapper.contract.documented.ContractCompletionMapper;
import com.samsung.framework.service.account.ghr.GhrAccountService;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.contract.ContractPaperVO;
import com.samsung.framework.vo.file.FilePublicVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractCompletionService {
    private final GhrAccountService ghrAccountService;
    private final ContractCompletionMapper contractCompletionMapper;
    private final FileService fileService;
    public ContractPaperVO paperContractSave(ContractPaperVO contract, AccountVO account, List<MultipartFile> file) throws Exception {
        GhrAccount ghrAccount = ghrAccountService.isExistsAccount(contract.getEmpNo());
        if(contract.getEmpNo() == ghrAccount.getEmpNo()){
            log.info("getEmpNo Equals");
            // 파일 업로드 (저장)
            List<FilePublicVO> list = fileService.uploadFile(file, "CONTRACT");
            List<FilePublicVO> targetList =fileService.saveFile(list, String.valueOf(account.getEmpNo()));
            FilePublicVO filePublicVO = targetList.get(0);

            //contractCompletionMapper.getTemplateSeq(contract); (임시 주석 처리)
            ContractPaperVO target = ContractPaperVO.builder()
                    .updatedBy(String.valueOf(account.getEmpNo()))
                    .createdBy(String.valueOf(account.getEmpNo()))
                    .deptCode(account.getDeptCode())
                    .contractBody("Dummy Data")
                    .signatureDataNo(String.valueOf(filePublicVO.getFileSeq()))
                    .docStatus(ContractProcessEnum.processCode(ContractProcessEnum.PAPER_CONTRACT))
                    .processStatus(ContractProcessEnum.processCode(ContractProcessEnum.PAPER_CONTRACT))
                    .validation("Y")
                    .agreeYn("N")
                    .delYn("N")
                    .templateSeq(1)
                    .empNo(ghrAccount.getEmpNo())
                    .build();
            contractCompletionMapper.paperContractSave(target);
        }
        return null;
    }
}
