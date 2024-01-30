package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.vo.contract.view.ContractView;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractsViewMapper {
    ContractView getContractView(ContractView o);

}
