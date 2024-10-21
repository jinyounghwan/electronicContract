package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.mapper.contract.documented.ContractCompletedMapper;
import com.samsung.framework.mapper.contract.documented.ContractSignRecallMapper;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.search.SearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractCompletedService {
    private final ContractCompletedMapper contractCompletedMapper;

    public int getContractSignCompletedCount(SearchVO o) {
        return contractCompletedMapper.getContractCompletedCount(o);
    }

    public List<ContractVO> getContractSignCompletedList(SearchVO searchVO) {
        List<ContractVO> list = contractCompletedMapper.getContractSignCompletedList(searchVO);
        list.forEach(data->{
            data.setFirstName(data.getName());
            data.setLastName("");
            int index = data.getName().indexOf(" ");
            if (index != -1) {
                try {
                    String lastName = StringUtil.getSubstring(data.getName(), 0, index);
                    String firstName = StringUtil.getSubstring(data.getName(), index);

                    data.setFirstName(firstName);
                    data.setLastName(lastName);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        return list;
    }

    public ContractVO getContractSignCompletedInfo(String seq) {
        ContractVO contractVO =  contractCompletedMapper.getContractSignCompletedInfo(seq);
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

}
