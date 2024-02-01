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
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.contract.template.Template;
import com.samsung.framework.vo.user.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractCreationService {

    private final ContractTemplateMapper contractTemplateMapper;
    private final AccountMapper accountMapper;
    private final FileService fileService;
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
        AccountVO o = AccountVO.builder().empNo(target.getEmpNo()).build();
        // user 정보 조회
        AccountVO user = accountMapper.myInfo(o);
        if(Objects.isNull(user)){
            return ObjectHandlingUtil.setSingleObjResultStatusVO(null, ResultCodeMsgEnum.INVALID_EMP_NO);
        }
        // 계약서 생성을 위해 데이터 셋팅
        ContractVO contractVO = ContractVO.builder()
                                .empNo(target.getEmpNo()).templateSeq(StringUtil.getInt(target.getTemplateCode()))
                .name(user.getName())
                .validation("Y")
                .agreeYn("N")
                .delYn("N")
                                .hireDateEn(StringUtil.getString(user.getEmployedAt())) //  헝가리, 영어 날짜로 변경
                                .hireDateHu(StringUtil.getString(user.getEmployedAt()))
                                .docStatus(ContractProcessEnum.processCode(ContractProcessEnum.UNSEEN))
                                .processStatus(ContractProcessEnum.processCode(ContractProcessEnum.CREATED))
                .contractDateHu(StringUtil.getString(target.getDate())) // format 변경
                .contractDateEn(StringUtil.getString(target.getDate()))
                                .deptCode(user.getDeptCode()).createdBy("admin")
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

    public List<Template> getTemplateCode() {
        return contractTemplateMapper.getTemplateCode();
    }
}
