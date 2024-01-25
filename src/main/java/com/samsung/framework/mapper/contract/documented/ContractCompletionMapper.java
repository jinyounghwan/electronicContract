package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.vo.contract.ContractPaperVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractCompletionMapper {
    int paperContractSave(ContractPaperVO contractPaperCompVO);
}
