package com.samsung.framework.mapper.contract;

import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractProgressMapper {
    int getContractProgressTotal(SearchVO searchVO);

    List<ContractVO> getContractProgresList(SearchVO searchVO);
}
