package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.vo.contract.view.ContractView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ContractsViewMapper {
    ContractView getContractView(@Param(value="contractNo") int contractNo);

}
