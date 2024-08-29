package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.account.AccountSearchVO;
import org.apache.ibatis.annotations.Mapper;

import java.io.File;
import java.util.List;

@Mapper
public interface EmployeeSignMapper {
    int getTotal(AccountSearchVO o);

    List<ContractVO> getContractWaitsList(AccountSearchVO searchVO);

    ContractVO getContractWaitInfo(ContractVO vo);

    int updateProcessStatus(ContractVO contractVO);

    int updateSignPath(ContractVO SignVo);
}
