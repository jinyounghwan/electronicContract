package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.mapper.contract.documented.ContractSignRecallMapper;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractSignRecallService {
    private final ContractSignRecallMapper contractSignRecallMapper;
    public int getContractSignRecallCount(SearchVO o) {
        return contractSignRecallMapper.getContractSignRecallCount(o);
    }

    public List<ContractVO> getContractSignRecallList(SearchVO searchVO) {
        List<ContractVO> list = contractSignRecallMapper.getContractSignRecallList(searchVO);
        list.forEach(e -> {e.setDocStatus(ContractProcessEnum.getProcessStatus(e.getDocStatus()));
            e.setProcessStatus(ContractProcessEnum.getProcessStatus(e.getProcessStatus()));
        });
        return list;
    }

    public ContractVO getContractSignRecallInfo(String seq) {
        ContractVO contractVO =  contractSignRecallMapper.getContractSignRecallInfo(seq);
        contractVO.setDocStatus(ContractProcessEnum.getProcessStatus(contractVO.getDocStatus()));
        contractVO.setProcessStatus(ContractProcessEnum.getProcessStatus(contractVO.getProcessStatus()));
        return contractVO;
    }
}
