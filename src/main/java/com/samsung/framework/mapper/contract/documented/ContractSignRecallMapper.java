package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractSignRecallMapper {
    int getContractSignRecallCount(SearchVO o);

    List<ContractVO> getContractSignRecallList(SearchVO searchVO);

    ContractVO getContractSignRecallInfo(String seq);
}
