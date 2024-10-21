package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractCompletedMapper {
    int getContractCompletedCount(SearchVO o);

    List<ContractVO> getContractSignCompletedList(SearchVO searchVO);

    ContractVO getContractSignCompletedInfo(String seq);
}
