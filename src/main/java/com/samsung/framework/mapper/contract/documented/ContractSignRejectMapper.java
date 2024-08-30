package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractSignRejectMapper {
    int getContractSignRejectCount(SearchVO o);

    List<ContractVO> getContractSignRejectList(SearchVO searchVO);

    ContractVO getContractSignRejectInfo(String seq);

    FilePublicVO getFileSeq(String seq);
}
