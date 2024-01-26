package com.samsung.framework.service.contract;

import com.samsung.framework.mapper.contract.ContractProgressMapper;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.contract.template.ContractTemplateVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractProgressService {
    private final ContractProgressMapper contractProgressMapper;
    public int getContractProgressTotal(SearchVO searchVO) {
        return contractProgressMapper.getContractProgressTotal(searchVO);
    }

    public List<ContractVO> getContractProgresList(SearchVO searchVO) {
        return contractProgressMapper.getContractProgresList(searchVO);
    }
}
