package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.contract.documented.ContractCreationMapper;
import com.samsung.framework.mapper.contract.documented.ContractsCreationMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.excel.ExcelPublicServiceImpl;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
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
import java.util.concurrent.atomic.AtomicInteger;

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

        AtomicInteger length = new AtomicInteger();
        for(List<ContractExcelVO> targetList : list){
            length.set(targetList.size());
            targetList.stream().iterator().forEachRemaining(data->{
                if(data.isValidation()){
                    // validation - 임직원 번호가 맞는지..등
                    AccountVO o = AccountVO.builder().empNo(StringUtil.getInt(data.getEmpNo())).build();
                    // user 정보 조회
                    AccountVO user = accountMapper.myInfo(o);
                    int templateSeq = contractTemplateMapper.getTemplateSeq(data.getTemplateCode());
                    ContractVO contractVO = ContractVO.builder()
                            .empNo(StringUtil.getInt(data.getEmpNo())).templateSeq(templateSeq)
                            .name(data.getName())
                            .validation("Y")
                            .agreeYn("N")
                            .delYn("N")
                            .hireDateEn(StringUtil.getString(user.getEmployedAt())) //  헝가리, 영어 날짜로 변경
                            .hireDateHu(StringUtil.getString(user.getEmployedAt()))
                            .docStatus(ContractProcessEnum.processCode(ContractProcessEnum.UNSEEN))
                            .processStatus(ContractProcessEnum.processCode(ContractProcessEnum.CREATED))
                            .contractDateHu(StringUtil.getString(data.getContractDateHu())) // format 변경
                            .contractDateEn(StringUtil.getString(data.getContractDateEn()))
                            .deptCode(user.getDeptCode()).createdBy("admin")
                            .build();
                    contractCreationMapper.saveContract(contractVO);
                }else{
                    length.set(0);
                    reusltMap.put( "code", ResultCodeMsgEnum.CREATE_DATA_FAIL.getMsg());
                    reusltMap.put( "msg", ResultCodeMsgEnum.CREATE_DATA_FAIL.getCode());
                    reusltMap.put("data" ,data.getRowNum());
                }
            });

        }
        return reusltMap;
    }
}
