package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.enums.MapKeyStringEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.*;
import com.samsung.framework.domain.common.Variables;
import com.samsung.framework.domain.contract.SaveContractRequest;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.contract.documented.ContractCreationMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.excel.ExcelPublicServiceImpl;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.contract.template.ContractTemplateVO;
import com.samsung.framework.vo.contract.template.Template;
import com.samsung.framework.vo.log.LogSaveResponse;
import com.samsung.framework.vo.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
    private final LogUtil logUtil;
    private final VariableHandlingUtil variableHandlingUtil;

    /**
     * 계약서 생성
     * @param target
     * @return
     */
    public ResultStatusVO saveContract(SaveContractRequest target , HttpServletRequest request) {
        // template 여부 체크
        ContractTemplateVO template = contractTemplateMapper.getContractTemplateInfo(StringUtil.getString(target.getTemplateCode()));
        if(Objects.isNull(template)){
            return ObjectHandlingUtil.setSingleObjResultStatusVO(null, ResultCodeMsgEnum.NO_TEMPLATE_CODE);
        }
        //  employee user 정보 조회
        AccountVO o = AccountVO.builder().empNo(target.getEmpNo()).build();
        AccountVO user = accountMapper.myInfo(o);
        if(Objects.isNull(user)){
            return ObjectHandlingUtil.setSingleObjResultStatusVO(null, ResultCodeMsgEnum.INVALID_EMP_NO);
        }
        // created by
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        Variables replacementTarget = Variables.builder().name(user.getName()).employeeNo(user.getEmpNo())
                                                 .contractDateEn(StringUtil.getString(target.getDate()))
                                                 .contractDateHu(StringUtil.getString(target.getDate()))
                                                 .salaryEn(target.getSalaryEn())
                                                 .salaryHu(target.getSalaryHu())
                                                 .hireDateEn(DateUtil.getFormatDate(StringUtil.getString(user.getEmployedAt()) ,"dd/mm/yyyy")) //  헝가리, 영어 날짜로 변경
                                                 .hireDateHu(DateUtil.getFormatDate(StringUtil.getString(user.getEmployedAt()) ,"yyyy.mm.dd"))
                                                 .build();
        // en title
        String replacedTitleEn  = variableHandlingUtil.replaceVariables(template.getContractTitleEn() , replacementTarget);
        // hu title
        String replacedTitleHu = variableHandlingUtil.replaceVariables(template.getContractTitleHu() , replacementTarget);
        // en content
        String replacedContentEn = variableHandlingUtil.replaceVariables(template.getContentsEn(), replacementTarget);
        // hu content
        String replacedContentHu = variableHandlingUtil.replaceVariables(template.getContentsHu() , replacementTarget);
        // en contract info
        String replacedContractInfoEn = "";
        if(!StringUtil.isEmpty(template.getContractInfoEn())){
            replacedContractInfoEn = variableHandlingUtil.replaceVariables(template.getContractInfoEn() , replacementTarget);
        }
        // hu contract info
        String replacedContractInfoHu = variableHandlingUtil.replaceVariables(template.getContractInfoHu() , replacementTarget);
        // en signatureArea
        String replacedEmployeeInfoEn = variableHandlingUtil.replaceVariables(template.getEmployeeInfoEn(), replacementTarget);
        // hu signatureArea
        String replacedEmployeeInfoHu = variableHandlingUtil.replaceVariables(template.getEmployeeInfoHu(), replacementTarget);
        // 계약서 생성을 위해 데이터 셋팅
        ContractVO contractVO = ContractVO.builder()
                                .empNo(target.getEmpNo()).templateSeq(StringUtil.getInt(target.getTemplateCode()))
                                .name(user.getName())
                                .validation("Y")
                                .agreeYn("N")
                                .delYn("N")
                                .hireDateEn(DateUtil.getFormatDate(StringUtil.getString(user.getEmployedAt()) ,"dd/mm/yyyy")) //  헝가리, 영어 날짜로 변경
                                .hireDateHu(DateUtil.getFormatDate(StringUtil.getString(user.getEmployedAt()) ,"yyyy.mm.dd"))
                                .salaryEn(target.getSalaryEn())
                                .salaryHu(target.getSalaryHu())
                                .docStatus(ContractProcessEnum.processCode(ContractProcessEnum.CREATED))
                                .processStatus(ContractProcessEnum.processCode(ContractProcessEnum.UNSEEN))
                                .contractDateHu(StringUtil.getString(target.getDate())) // format 변경
                                .contractDateEn(StringUtil.getString(target.getDate()))
                                .deptCode(user.getDeptCode()).createdBy(account.getAdminId())
                .templateDetailsSeq(template.getTemplateDetailsSeq())
                .contractTitleEn(replacedTitleEn)
                .contractTitleHu(replacedTitleHu)
                .contentsEn(replacedContentEn)
                .contentsHu(replacedContentHu)
                .contractInfoEn(replacedContractInfoEn)
                .contractInfoHu(replacedContractInfoHu)
                .employeeInfoEn(replacedEmployeeInfoEn)
                .employeeInfoHu(replacedEmployeeInfoHu)
                                .build();
        int saveContract = contractCreationMapper.saveContract(contractVO);
        if(saveContract == 0){
            return new ResultStatusVO(ResultCodeMsgEnum.CREATE_DATA_FAIL.getCode(),ResultCodeMsgEnum.CREATE_DATA_FAIL.name());
        }
        saveContract = contractCreationMapper.saveContractDetail(contractVO);
        if(saveContract == 0){
            return new ResultStatusVO(ResultCodeMsgEnum.CREATE_DATA_FAIL.getCode(),ResultCodeMsgEnum.CREATE_DATA_FAIL.name());
        }
        // 저장이 성공 되었을 때
        LogSaveRequest saveRequest = LogSaveRequest.builder().logType(LogTypeEnum.LOG_CREATE)
                                                             .processStep(LogTypeEnum.LOG_CREATE.getDescription())
                                                             .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                                                             .createdBy(account.getAdminId())
                                                             .contractNo(StringUtil.getString(contractVO.getContractNo()))
                                                             .build();
        Map<String, LogSaveResponse> logs = logUtil.saveLog(saveRequest);
        return new ResultStatusVO();
    }

    private boolean isTemplateCodeExists(int templateCode) {
        if (contractTemplateMapper.getContractTemplateCode(templateCode) < 1) {
            return false;
        }
        return true;
    }

    /**
     * template code list
     * */
    public List<Template> getTemplateCode() {
        return contractTemplateMapper.getTemplateCode();
    }
}
