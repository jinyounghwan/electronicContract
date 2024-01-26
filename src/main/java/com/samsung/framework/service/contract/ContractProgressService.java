package com.samsung.framework.service.contract;

import com.samsung.framework.mapper.contract.ContractProgressMapper;
import com.samsung.framework.vo.search.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractProgressService {
    private final ContractProgressMapper contractProgressMapper;
    public int getContractProgressTotal(SearchVO searchVO) {
        return contractProgressMapper.getContractProgressTotal(searchVO);
    }
}
