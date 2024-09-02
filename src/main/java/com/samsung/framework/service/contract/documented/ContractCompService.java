package com.samsung.framework.service.contract.documented;


import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.domain.account.ghr.GhrAccount;
import com.samsung.framework.domain.contract.paper.ContractComp;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.mapper.contract.documented.ContractCompletionMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.account.ghr.GhrAccountService;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.contract.completion.ContractCompVO;
import com.samsung.framework.vo.contract.template.Template;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractCompService {
    private final GhrAccountService ghrAccountService;
    private final ContractTemplateMapper contractTemplateMapper;
    private final ContractCompletionMapper contractCompletionMapper;
    private final FileService fileService;
    private final LogUtil logUtil;
    public Map<String, Object> paperContractSave(HttpServletRequest request, ContractCompVO contract, AccountVO account, List<MultipartFile> file) throws Exception {
        var result = new HashMap<String, Object>();
        GhrAccount ghrAccount = ghrAccountService.isExistsAccount(contract.getEmpNo());

        if(contract.getEmpNo() == ghrAccount.getEmpNo()){
            // 시간 변경
            String hireDateHu = DateUtil.convertLocalDateTimeToString(ghrAccount.getHireDate(),"YYYY-MM-dd");
            String hireDateEn = DateUtil.convertLocalDateTimeToString(ghrAccount.getHireDate(),"MM/dd/YYYY");

            // 파일 업로드 (저장)
            List<FilePublicVO> list = fileService.uploadFile(file, "Contract");
            List<FilePublicVO> targetList =fileService.saveFile(list, String.valueOf(account.getEmpNo()));
            FilePublicVO filePublicVO = targetList.get(0);

            //contractCompletionMapper.getTemplateSeq(contract); (임시 주석 처리 templateSeq 가져오는 부분)
            ContractComp target = ContractComp.builder()
                    .updatedBy(String.valueOf(account.getEmpNo()))
                    .createdBy(String.valueOf(account.getEmpNo()))
                    .deptCode(ghrAccount.getDepartmentCode())
                    .name(ghrAccount.getName())
                    .signatureDataNo(String.valueOf(filePublicVO.getFileSeq()))
                    .docStatus(ContractProcessEnum.processCode(ContractProcessEnum.PAPER_CONTRACT))
                    .processStatus(ContractProcessEnum.processCode(ContractProcessEnum.SIGNED))
                    .hireDateHu(hireDateHu)
                    .hireDateEn(hireDateEn)
                    .contractDateHu(contract.getContractDate())
                    .contractDateEn(DateUtil.getStrContractDateEn(contract.getContractDate()))
                    .validation("Y")
                    .agreeYn("N")
                    .delYn("N")
                    .templateSeq(contract.getTemplateSeq())
                    .empNo(ghrAccount.getEmpNo())
                    .build();
            result.put("code", 200);

            int insert = contractCompletionMapper.paperContractSave(target);

            if(insert < 1) {
                result.put("code", 204);
                result.put("message", "계약서를 다시 저장해주세요.");
                return result;
            }

            var logSaveRequest = LogSaveRequest.builder()
                    .logType(ContractProcessEnum.getProcessStatus(ContractProcessEnum.processCode(ContractProcessEnum.LOG_SIGN_N_COMPLETE)))
                    .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                    .createdBy(String.valueOf(account.getEmpNo()))
                    .contractNo(String.valueOf(target.getContractNo()))
                    .processStep(ContractProcessEnum.processCode(ContractProcessEnum.LOG_PAPER_SIGN))
                    .build();

            logUtil.saveLog(logSaveRequest);
            result.put("message", "계약서 저장 완료");
            return result;
        }

        result.put("code",400);
        result.put("message", "GHR에 존재하지 않는 사번입니다.");
        return result;
    }

    public int getContractCompTotal(SearchVO searchVO) {
        int count = contractCompletionMapper.getContractCompTotal(searchVO);
        return count;
    }

    public List<ContractCompVO> getContractCompList(SearchVO searchVO){
        List<ContractCompVO> list = contractCompletionMapper.getContractCompList(searchVO);
        list.forEach(data->{
            data.setCreatedAtStr(DateUtil.convertLocalDateTimeToString(data.getCreatedAt(), "yyyy-MM-dd"));
            data.setUpdatedAtStr(DateUtil.convertLocalDateTimeToString(data.getUpdatedAt(), "yyyy-MM-dd"));
            data.setSignDateAtStr(DateUtil.convertLocalDateTimeToString(data.getSignDate(), DateUtil.DATETIME_YMDHM_PATTERN));
            data.setDocStatus(String.valueOf(ContractProcessEnum.getProcessStatus(data.getDocStatus())));
            data.setProcessStatus(String.valueOf(ContractProcessEnum.getProcessStatus(data.getProcessStatus())));
        });
        return list;
    }

   public ContractCompVO getContractCompDetail(long contractNo){
        ContractCompVO contract = contractCompletionMapper.getContractCompDetail(contractNo);
        contract.setCreatedAtStr(DateUtil.convertLocalDateTimeToString(contract.getCreatedAt(), DateUtil.DATETIME_YMDHM_PATTERN));
        contract.setUpdatedAtStr(DateUtil.convertLocalDateTimeToString(contract.getUpdatedAt(), DateUtil.DATETIME_YMDHM_PATTERN));
        contract.setSignDateAtStr(DateUtil.convertLocalDateTimeToString(contract.getSignDate(), DateUtil.DATETIME_YMDHM_PATTERN));
        contract.setAssignDateAtStr(DateUtil.convertLocalDateTimeToString(contract.getAssignedAt(), DateUtil.DATETIME_YMDHM_PATTERN));
        contract.setDocStatus(String.valueOf(ContractProcessEnum.getProcessStatus(contract.getDocStatus())));
        contract.setProcessStatus(String.valueOf(ContractProcessEnum.getProcessStatus(contract.getProcessStatus())));
        return contract;
   }

    public List<Template> getTemplateCode() {
        return contractTemplateMapper.getTemplateCode();
    }

    public FilePublicVO getFileSeq(String seq) {
        log.info("seq >> " + seq);
        FilePublicVO filePublicVO = contractCompletionMapper.getFileSeq(seq);
        log.info("filePublicVo >> " + filePublicVO);
        return filePublicVO;
    }
}
