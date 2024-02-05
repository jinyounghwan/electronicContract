package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractProgressMapper {
    int getContractProgressTotal(SearchVO searchVO);

    List<ContractVO> getContractProgressList(SearchVO searchVO);

    int updateContractDocStatus(List<ProgressRequest> progressRequest);

    ContractVO getContractProgressInfo(String seq);

    int updateContractDocStatusInfo(ProgressRequest progressRequest);
}
