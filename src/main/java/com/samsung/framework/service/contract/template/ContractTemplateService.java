package com.samsung.framework.service.contract.template;

import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.domain.contract.SaveContractRequest;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.mapper.member.MemberMapper;
import com.samsung.framework.vo.common.ResultStatusVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractTemplateService {

    private final ContractTemplateMapper contractTemplateMapper;
    private final MemberMapper memberMapper;

    /**
     * 계약서 생성
     * @param target
     * @return
     */
    public ResultStatusVO saveContract(SaveContractRequest target) {
        log.info(target.toString());
        
        // IJ TODO: 템플릿 코드 검증
        if(!isTemplateCodeExists(Integer.parseInt(target.getTemplateCode()))) {
            return new ResultStatusVO(ResultCodeMsgEnum.NO_TEMPLATE_CODE.getCode(), ResultCodeMsgEnum.NO_TEMPLATE_CODE.getMsg());
        }

        // IJ TODO: 임직원 번호 검증 NOTE:매퍼 파일 변경 가능성 있음.
        if (!memberMapper.existsByEmpNo(Long.valueOf(target.getEmpNo()))) {
            return new ResultStatusVO(ResultCodeMsgEnum.INVALID_EMP_NO.getCode(), ResultCodeMsgEnum.INVALID_EMP_NO.getMsg());
        }

        // IJ TODO: 계약서 생성
        return new ResultStatusVO();
    }

    private boolean isTemplateCodeExists(int templateCode) {
        if (contractTemplateMapper.getContractTemplateCode(templateCode) < 1) {
            return false;
        }
        return true;
    }
}
