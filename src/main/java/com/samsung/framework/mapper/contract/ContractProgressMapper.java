package com.samsung.framework.mapper.contract;

import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractProgressMapper {
    int getContractProgressTotal(SearchVO searchVO);
}
