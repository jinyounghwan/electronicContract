package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.mapper.contract.documented.ContractSignRejectMapper;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractSignRejectService {
    private  final ContractSignRejectMapper contractSignRejectMapper;

    public int getContractSignRejectCount(SearchVO o) {
        return contractSignRejectMapper.getContractSignRejectCount(o);
    }

    public List<ContractVO> getContractSignRejectList(SearchVO searchVO) {
        List<ContractVO> list = contractSignRejectMapper.getContractSignRejectList(searchVO);
        list.forEach(e -> {e.setDocStatus(ContractProcessEnum.getProcessStatus(e.getDocStatus()));
            e.setProcessStatus(ContractProcessEnum.getProcessStatus(e.getProcessStatus()));
        });
        return list;
    }

    public ContractVO getContractSignRejectInfo(String seq) {
        ContractVO contractVO =  contractSignRejectMapper.getContractSignRejectInfo(seq);
        contractVO.setDocStatus(ContractProcessEnum.getProcessStatus(contractVO.getDocStatus()));
        contractVO.setProcessStatus(ContractProcessEnum.getProcessStatus(contractVO.getProcessStatus()));
        return contractVO;
    }

    public FilePublicVO getFileSeq(String seq) {
        log.info("seq >> " + seq);
        FilePublicVO filePublicVO = contractSignRejectMapper.getFileSeq(seq);
        log.info("filePublicVo >> " + filePublicVO);
        return filePublicVO;
    }
}
