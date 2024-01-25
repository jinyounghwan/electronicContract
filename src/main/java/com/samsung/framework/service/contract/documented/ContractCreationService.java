package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.ObjectHandlingUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.domain.contract.SaveContractRequest;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.contract.documented.ContractCreationMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.excel.ExcelPublicServiceImpl;
import com.samsung.framework.service.file.FilePublicServiceImpl;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
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
    private final ContractCreationMapper contractCreationMapper;

    /**
     * 계약서 생성
     * @param target
     * @return
     */
    public ResultStatusVO saveContract(SaveContractRequest target) {
        if(!isTemplateCodeExists(Integer.parseInt(target.getTemplateCode()))) {
            return ObjectHandlingUtil.setSingleObjResultStatusVO(null, ResultCodeMsgEnum.NO_TEMPLATE_CODE);
        }
        // TODO: 임직원 번호 검증
        //  TODO: 임직원 부서코드 조회
        //  NOTE:매퍼 파일 변경 가능성 있음.
//        if (!accountMapper.existsByEmpNo(target.getEmpNo())) {
//            return ObjectHandlingUtil.setSingleObjResultStatusVO(null, ResultCodeMsgEnum.INVALID_EMP_NO);
//        }
        ContractVO contractVO = ContractVO.builder()
                                .empNo(target.getEmpNo()).templateSeq(StringUtil.getInt(target.getTemplateCode()))
                                .processStep(ContractProcessEnum.processCode(ContractProcessEnum.CREATED))
                                .deptCode("dept00001").createdBy("admin")
                                .build();
        int result = contractCreationMapper.saveContract(contractVO);
        if(result == 0){
            return new ResultStatusVO(ResultCodeMsgEnum.CREATE_DATA_FAIL.getCode(),ResultCodeMsgEnum.CREATE_DATA_FAIL.name());
        }
        return new ResultStatusVO();
    }

    private boolean isTemplateCodeExists(int templateCode) {
        if (contractTemplateMapper.getContractTemplateCode(templateCode) < 1) {
            return false;
        }
        return true;
    }
}
