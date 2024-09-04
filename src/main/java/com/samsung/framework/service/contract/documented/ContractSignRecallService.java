package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.mapper.contract.documented.ContractSignRecallMapper;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.file.FilePublicVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractSignRecallService {
    private final ContractSignRecallMapper contractSignRecallMapper;
    private final LogUtil logUtil;
    public int getContractSignRecallCount(SearchVO o) {
        return contractSignRecallMapper.getContractSignRecallCount(o);
    }

    public List<ContractVO> getContractSignRecallList(SearchVO searchVO) {
        List<ContractVO> list = contractSignRecallMapper.getContractSignRecallList(searchVO);
        list.forEach(e -> {e.setDocStatus(ContractProcessEnum.getProcessStatus(e.getDocStatus()));
            e.setProcessStatus(ContractProcessEnum.getProcessStatus(e.getProcessStatus()));

            e.setFirstName(e.getName());
            e.setLastName("");
            int index = e.getName().indexOf(" ");
            if (index != -1) {
                try {
                    String lastName = StringUtil.getSubstring(e.getName(), 0, index);
                    String firstName = StringUtil.getSubstring(e.getName(), index);

                    e.setFirstName(firstName);
                    e.setLastName(lastName);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        return list;
    }

    public ContractVO getContractSignRecallInfo(String seq) {
        ContractVO contractVO =  contractSignRecallMapper.getContractSignRecallInfo(seq);
        contractVO.setDocStatus(ContractProcessEnum.getProcessStatus(contractVO.getDocStatus()));
        contractVO.setProcessStatus(ContractProcessEnum.getProcessStatus(contractVO.getProcessStatus()));

        contractVO.setFirstName(contractVO.getName());
        contractVO.setLastName("");
        int index = contractVO.getName().indexOf(" ");
        if (index != -1) {
            try {
                String lastName = StringUtil.getSubstring(contractVO.getName(), 0, index);
                String firstName = StringUtil.getSubstring(contractVO.getName(), index);

                contractVO.setFirstName(firstName);
                contractVO.setLastName(lastName);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return contractVO;
    }

    public FilePublicVO getFileSeq(String seq) {
        log.info("seq >> " + seq);
        FilePublicVO filePublicVO = contractSignRecallMapper.getFileSeq(seq);
        log.info("filePublicVo >> " + filePublicVO);
        return filePublicVO;
    }
}
