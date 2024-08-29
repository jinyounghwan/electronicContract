package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.ContractTemplateEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.*;
import com.samsung.framework.domain.common.Variables;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.contract.documented.ContractCreationMapper;
import com.samsung.framework.mapper.contract.documented.ContractsCreationMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.excel.ExcelPublicServiceImpl;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.BulkExcelVO;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.ContractExcelVO;
import com.samsung.framework.vo.contract.completion.ContractCompVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.contract.template.ContractTemplateVO;
import com.samsung.framework.vo.contract.view.ContractView;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.log.LogSaveResponse;
import com.samsung.framework.vo.search.SearchVO;
import com.samsung.framework.vo.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractsCreationService {
    private ContractsCreationMapper contractsCreationMapper;
    private final ContractTemplateMapper contractTemplateMapper;
    private final AccountMapper accountMapper;
    private final FileService fileService;
    private final ExcelPublicServiceImpl excelService;
    private final ContractCreationMapper contractCreationMapper;
    private final ValidationUtil validationUtil;
    private final VariableHandlingUtil variableHandlingUtil;
    private final LogUtil logUtil;
    /**
     * 계약서 일괄 생성
     * @param multipartFiles
     * @return
     * @throws Exception
     */
    public Map<String, Object> bulkUpload(List<MultipartFile> multipartFiles , HttpServletRequest request) throws Exception {
        Map<String,Object> reusltMap = new HashMap<String,Object>();
        // created by
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");

        List<FilePublicVO> fileList = fileService.uploadFile(multipartFiles, "Contract/Excel");
        Map<String,Object> dataMap = new HashMap<String,Object>();
        if(fileList.isEmpty()) {
            reusltMap.put( "code", ResultCodeMsgEnum.CREATE_DATA_FAIL.getMsg());
            reusltMap.put( "msg", ResultCodeMsgEnum.CREATE_DATA_FAIL.getCode());
            reusltMap.put("data", dataMap);
            return reusltMap;
        }

        // 엑셀 조회
        List<List<ContractExcelVO>> list  = excelService.readExcelFile(fileList);

        log.info("333333");
        // 어떤 사번에서 빈값이 들어갔는지
        BulkExcelVO bulkExcelVO = validationUtil.excelBulkDataValidator(list.get(0));
        if(!"00000000".equals(bulkExcelVO.getEmpNo())){
            reusltMap.put("code", ResultCodeMsgEnum.CREATE_DATA_FAIL.getCode());
            reusltMap.put("msg", ResultCodeMsgEnum.CREATE_DATA_FAIL.getMsg());
            dataMap.put("empNo" , bulkExcelVO.getEmpNo());
            dataMap.put("alertMessage"  , bulkExcelVO.getMsg());
            reusltMap.put("data",dataMap );
            return reusltMap;
        }
        for(List<ContractExcelVO> targetList : list){
            targetList.stream().iterator().forEachRemaining(data->{
                UserVO user = accountMapper.getUserInfo(data.getEmpNo());
                log.info("data.getSalaryEn() >> " + data.getSalaryEn());
                log.info("data.getSalaryHu() >> " + data.getSalaryHu());

                // template 여부 체크
                ContractTemplateVO template = contractTemplateMapper.getContractTemplateInfo(StringUtil.getString(data.getTemplateCode()));
                Variables replacementTarget = Variables.builder().name(user.getName()).employeeNo(StringUtil.getString(user.getEmpNo()))
                        .contractDateEn(DateUtil.getStrContractDateEn(StringUtil.getString(data.getContractDate())))
                        .contractDateHu(StringUtil.getString(data.getContractDate()).replaceAll("-","."))
                        .salaryEn(data.getSalaryEn())
                        .salaryHu(data.getSalaryHu())
                        .hireDateEn(user.getHireDateEn())
                        .hireDateHu(user.getHireDateHu())
                        .jobTitleEn(user.getJobTitle())
                        .jobTitleHu(user.getJobTitle())
                        .salaryNo(user.getSalaryNo())
                        .wageTypeEn(user.getWageType())
                        .wageTypeHu(replaceWageType(user.getWageType()))
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
                        .empNo(data.getEmpNo()).templateSeq(StringUtil.getInt(data.getTemplateCode()))
                        .name(user.getName())
                        .validation("Y")
                        .agreeYn("N")
                        .delYn("N")
                        .hireDateEn(user.getHireDateEn()) //  헝가리, 영어 날짜로 변경
                        .hireDateHu(user.getHireDateHu())
                        .salaryEn(data.getSalaryEn())
                        .salaryHu(data.getSalaryHu())
                        .docStatus(ContractProcessEnum.processCode(ContractProcessEnum.CREATED))
                        .processStatus(ContractProcessEnum.processCode(ContractProcessEnum.UNSEEN))
                        .contractDateHu(data.getContractDate().replaceAll("-","."))
                        .contractDateEn(DateUtil.getStrContractDateEn(StringUtil.getString(data.getContractDate())))
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
                saveContract = contractCreationMapper.saveContractDetail(contractVO);
                // 저장이 성공 되었을 때
                LogSaveRequest saveRequest = LogSaveRequest.builder().logType(LogTypeEnum.LOG_CREATE)
                        .processStep(LogTypeEnum.LOG_CREATE.getDescription())
                        .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                        .createdBy(account.getAdminId())
                        .contractNo(StringUtil.getString(contractVO.getContractNo()))
                        .build();
                Map<String, LogSaveResponse> logs = logUtil.saveLog(saveRequest);
            });
        }
        log.info("4444444");
        reusltMap.put("code", ResultCodeMsgEnum.REQUEST_SUCCESS.getCode());
        reusltMap.put("msg", ResultCodeMsgEnum.REQUEST_SUCCESS.getMsg());
        dataMap.put("totalCount" ,list.get(0).size());
        reusltMap.put("data",dataMap );
        return reusltMap;
    }
    private String replaceWageType(String type){
        String replaceType ="";
        switch (type) {
            case "M" -> {
                replaceType =  "hó";
            }
            case "H" -> {
                replaceType =  "óra";
            }
            default -> {
                replaceType = "";
            }
        }
        return replaceType;
    }

    public List<ContractTemplateVO> getExcelSelect(List<Integer> excelList2){

        List<ContractTemplateVO> list = contractTemplateMapper.getExcelSelect(excelList2);

       /* list.forEach(data->{
            data.setCreatedAtStr(DateUtil.convertLocalDateTimeToString(data.getCreatedAt(), "yyyy-MM-dd"));
            data.setUpdatedAtStr(DateUtil.convertLocalDateTimeToString(data.getUpdatedAt(), "yyyy-MM-dd"));
            data.setSignDateAtStr(DateUtil.convertLocalDateTimeToString(data.getSignDate(), DateUtil.DATETIME_YMDHM_PATTERN));
            data.setDocStatus(String.valueOf(ContractProcessEnum.getProcessStatus(data.getDocStatus())));
            data.setProcessStatus(String.valueOf(ContractProcessEnum.getProcessStatus(data.getProcessStatus())));
        });*/
        return list;
    }


}
