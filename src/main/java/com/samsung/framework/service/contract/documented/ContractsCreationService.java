package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.ContractTemplateEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.contract.documented.ContractsCreationMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.excel.ExcelPublicServiceImpl;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.ContractExcelVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.file.FilePublicVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractsCreationService {
    private ContractsCreationMapper contractsCreationMapper;
    private final ContractTemplateMapper contractTemplateMapper;
    private final AccountMapper accountMapper;
    private final FileService fileService;
    private final ExcelPublicServiceImpl excelService;

    /**
     * 계약서 일괄 생성
     * @param multipartFiles
     * @return
     * @throws Exception
     */
    public Map<String, Object> bulkUpload(List<MultipartFile> multipartFiles) throws Exception {
        Map<String,Object> reusltMap = new HashMap<String,Object>();
        List<FilePublicVO> fileList = fileService.uploadFile(multipartFiles, "Contract/Excel");
        if(fileList.isEmpty()) {
            reusltMap.put( "code", ResultCodeMsgEnum.CREATE_DATA_FAIL.getMsg());
            reusltMap.put( "msg", ResultCodeMsgEnum.CREATE_DATA_FAIL.getCode());
        }
        // 엑셀 조회
        List<List<ContractExcelVO>> list  = excelService.readExcelFile(fileList);
        // 몇건 인지 , 어디서 실패 했는지
        log.info("========================> :{}" , list);
        for(List<ContractExcelVO> targetList : list){
            targetList.stream().iterator().forEachRemaining(data->{
                if(data.isValidation()){
                    // validation - 임직원 번호가 맞는지..등
                    AccountVO o = AccountVO.builder().empNo(StringUtil.getInt(data.getEmpNo())).build();
                    // user 정보 조회
                    AccountVO user = accountMapper.myInfo(o);
                    ContractVO contractVO = ContractVO.builder()
                            .empNo(StringUtil.getInt(data.getEmpNo())).templateSeq(StringUtil.getInt(data.getTemplateCode()))
                            .name(data.getName())
                            .validation("Y")
                            .agreeYn("N")
                            .delYn("N")
                            .hireDateEn(StringUtil.getString(o.getEmployedAt())) //  헝가리, 영어 날짜로 변경
                            .hireDateHu(StringUtil.getString(o.getEmployedAt()))
                            .docStatus(ContractProcessEnum.processCode(ContractProcessEnum.UNSEEN))
                            .processStatus(ContractProcessEnum.processCode(ContractProcessEnum.CREATED))
                            //.contractDateHu(StringUtil.getString(data.getDate())) // format 변경
                            //.contractDateEn(StringUtil.getString(data.getDate()))
                            .deptCode(o.getDeptCode()).createdBy("admin")
                            .build();
                    //int result = contractCreationMapper.saveContract(contractVO);
                }
            });
        }
        return reusltMap;
    }
}
