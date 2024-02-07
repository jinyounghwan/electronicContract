package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.ContractTemplateEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.common.utils.ValidationUtil;
import com.samsung.framework.mapper.account.AccountMapper;
import com.samsung.framework.mapper.contract.documented.ContractCreationMapper;
import com.samsung.framework.mapper.contract.documented.ContractsCreationMapper;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.service.excel.ExcelPublicServiceImpl;
import com.samsung.framework.service.file.FileService;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.BulkExcelVO;
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
    private final ContractCreationMapper contractCreationMapper;
    private final ValidationUtil validationUtil;

    /**
     * 계약서 일괄 생성
     * @param multipartFiles
     * @return
     * @throws Exception
     */
    public Map<String, Object> bulkUpload(List<MultipartFile> multipartFiles) throws Exception {
        Map<String,Object> reusltMap = new HashMap<String,Object>();
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

        // 어떤 사번에서 빈값이 들어갔는지
        BulkExcelVO bulkExcelVO = validationUtil.excelBulkDataValidator(list.get(0));
        if(!"00000000".equals(bulkExcelVO.getEmpNo())){
            reusltMap.put("code", ResultCodeMsgEnum.CREATE_DATA_FAIL.getCode());
            reusltMap.put("msg", ResultCodeMsgEnum.CREATE_DATA_FAIL.getMsg());
            dataMap.put("empNo" , bulkExcelVO.getEmpNo());
            reusltMap.put("data",dataMap );
            return reusltMap;
        }
        for(List<ContractExcelVO> targetList : list){
            targetList.stream().iterator().forEachRemaining(data->{
                // validation - 임직원 번호가 맞는지..등
                AccountVO o = AccountVO.builder().empNo(data.getEmpNo()).build();
                // user 정보 조회
                AccountVO user = accountMapper.myInfo(o);
                ContractVO contractVO = ContractVO.builder()
                        .empNo(data.getEmpNo()).templateSeq(StringUtil.getInt(data.getTemplateSeq()))
                        .name(data.getName())
                        .validation("Y")
                        .agreeYn("N")
                        .delYn("N")
                        .hireDateEn(data.getHireDateEn())
                        .hireDateHu(data.getHireDateHu())
                        .docStatus(ContractProcessEnum.processCode(ContractProcessEnum.CREATED))
                        .processStatus(ContractProcessEnum.processCode(ContractProcessEnum.UNSEEN))
                        .contractDateHu(StringUtil.getString(data.getContractDateHu())) // format 변경
                        .contractDateEn(StringUtil.getString(data.getContractDate()))
                        .deptCode(user.getDeptCode()).createdBy("admin")
                        .build();
                contractCreationMapper.saveContract(contractVO);
            });
        }
        reusltMap.put("code", ResultCodeMsgEnum.REQUEST_SUCCESS.getCode());
        reusltMap.put("msg", ResultCodeMsgEnum.REQUEST_SUCCESS.getMsg());
        dataMap.put("totalCount" ,list.get(0).size());
        reusltMap.put("data",dataMap );
        return reusltMap;
    }
}
