package com.samsung.framework.service.contract;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.mapper.contract.ContractProgressMapper;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.contract.template.ContractTemplateVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractProgressService {
    private final ContractProgressMapper contractProgressMapper;
    public int getContractProgressTotal(SearchVO searchVO) {
        return contractProgressMapper.getContractProgressTotal(searchVO);
    }

    public List<ContractVO> getContractProgressList(SearchVO searchVO) {
        List<ContractVO> list = contractProgressMapper.getContractProgressList(searchVO);
        list.forEach(e -> {e.setDocStatus(ContractProcessEnum.getProcessStatus(e.getDocStatus()));
        });
        return list;
    }

    public ResultStatusVO updateContractRecall(List<ProgressRequest> progressRequest) {
        progressRequest.forEach(el -> el.setDocStatus(ContractProcessEnum.processCode(ContractProcessEnum.RECALLED)));
        int result = contractProgressMapper.updateContractRecall(progressRequest);
        if(result == 0 ){
            return new ResultStatusVO(ResultCodeMsgEnum.UPDATE_DATA_FAIL.getCode(), ResultCodeMsgEnum.UPDATE_DATA_FAIL.getMsg());
        }
        return new ResultStatusVO();
    }
}
