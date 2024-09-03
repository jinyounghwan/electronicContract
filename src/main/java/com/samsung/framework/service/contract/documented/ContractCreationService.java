package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.*;
import com.samsung.framework.domain.common.Variables;
import com.samsung.framework.domain.contract.CreateViewContract;
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
import com.samsung.framework.vo.contract.view.ContractView;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.log.LogSaveResponse;
import com.samsung.framework.vo.user.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractCreationService {
    private final String PDF_STORAGE_PATH= File.separator+ "Contract" + File.separator + "PDF" +File.separator;

    @Value("${properties.file.rootDir}")
    private String getRootDir;

    @Value("${properties.file.realDir}")
    private String getRealDir;


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
    public ResultStatusVO saveContract(SaveContractRequest target , HttpServletRequest request) throws Exception {
        log.info("Create 1");

        // template 여부 체크
        ContractTemplateVO template = contractTemplateMapper.getContractTemplateInfo(StringUtil.getString(target.getTemplateCode()));
        if(Objects.isNull(template)){
            return ObjectHandlingUtil.setSingleObjResultStatusVO(null, ResultCodeMsgEnum.NO_TEMPLATE_CODE);
        }
        //  employee user 정보 조회
//        AccountVO user = accountMapper.myInfo(o);
        UserVO user = accountMapper.getUserInfo(target.getEmpNo());

        log.info("target Data Service >> " + target);
        log.info("user Data Service >> " + user);

        if(Objects.isNull(user)){
            return ObjectHandlingUtil.setSingleObjResultStatusVO(null, ResultCodeMsgEnum.INVALID_EMP_NO);
        }
        // created by
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        Variables replacementTarget = Variables.builder().name(user.getName()).employeeNo(StringUtil.getString(user.getEmpNo()))
                .contractDateEn(DateUtil.getStrContractDateEn(StringUtil.getString(target.getDate())))
                .contractDateHu(StringUtil.getString(target.getDate()).replaceAll("-","."))
                .salaryEn(target.getSalaryEn())
                .salaryHu(target.getSalaryHu())
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
                .empNo(target.getEmpNo()).templateSeq(StringUtil.getInt(target.getTemplateCode()))
                .name(user.getName())
                .validation("Y")
                .agreeYn("N")
                .delYn("N")
                .hireDateEn(user.getHireDateEn()) //  헝가리, 영어 날짜로 변경
                .hireDateHu(user.getHireDateHu())
                .salaryEn(target.getSalaryEn())
                .salaryHu(target.getSalaryHu())
                .docStatus(ContractProcessEnum.processCode(ContractProcessEnum.CREATED))
                .processStatus(ContractProcessEnum.processCode(ContractProcessEnum.UNSEEN))
                .contractDateEn(DateUtil.getStrContractDateEn(StringUtil.getString(target.getDate())))
                .contractDateHu(StringUtil.getString(target.getDate()).replaceAll("-","."))
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
        log.info("Create 2");

        // File Path

        FilePublicVO fileVO = new FilePublicVO();
        String createFileName = FileUtil.createPdfFileName();                                               //파일 이름 생성
        String nowDay = DateUtil.getUtcNowDateFormat("yyMM");                                        //현재 날짜
        String storagePath = FileUtil.getOsRootDir() + getRootDir + getRealDir + PDF_STORAGE_PATH + nowDay; // 파일 저장 위치 설정
        String paths = storagePath + File.separator + target.getParamPath();                                // 최종 파일 위치
        long fileSeq = Long.parseLong(String.valueOf(contractVO.getContractNo()));
        String Size = "1";

        log.info("contractVO.getContractNo() >> " + contractVO.getContractNo());
        log.info("fileSeq >> " + fileSeq);

        log.info("paths >> " + paths);
        log.info("storagePath >> " + storagePath);
        log.info("File.separator >> " + File.separator);
        log.info("createFileName>> " + createFileName);


        //파일경로 추가 param Setting
        fileVO.setFileSeq(fileSeq); // file Seq
        fileVO.setOriginalName(target.getParamPath()); // 원본 파일명
        fileVO.setName(target.getParamPath());      // 파일 명
        fileVO.setFileNo(contractVO.getContractNo()); //파일번호
        fileVO.setExtension("pdf"); // 확장자
        fileVO.setStoragePath(paths);              // 파일경로
        fileVO.setDelYn("N"); // 삭제여부
        fileVO.setCreatedBy(user.getName());

        log.info("fileVO >> " + fileVO);

        // file db 저장
        contractCreationMapper.saveFilePath(fileVO);

        log.info("Create 3");
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

    /**
     * 계약서 생성전 employId 검증로직 추가
     */
    public int getEmployCheck(CreateViewContract param) {
        return contractTemplateMapper.getEmployCheck(param.getEmployeeId());
    }
    /**
     * 계약서 생성 View Contract
     * */
    public ContractView getCreateContractView(CreateViewContract param) {
        return contractTemplateMapper.getCreateContractView(param.getTemplateSeq());
    }
}