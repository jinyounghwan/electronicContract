package com.samsung.framework.mapper.contract.documented;

import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.file.FilePublicVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractCreationMapper {

    int saveContract(ContractVO contractVO);

    int saveContractDetail(ContractVO contractVO);

    void saveFilePath(FilePublicVO file);
}
