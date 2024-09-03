package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.common.utils.VariableHandlingUtil;
import com.samsung.framework.domain.common.Variables;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.mapper.contract.documented.ContractsViewMapper;
import com.samsung.framework.mapper.log.LogMapper;
import com.samsung.framework.vo.contract.view.ContractView;
import com.samsung.framework.vo.contract.view.HistoryVO;
import com.samsung.framework.vo.contract.view.ViewInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractsViewService {
    private final ContractsViewMapper contractsViewMapper;
    private final VariableHandlingUtil variableHandlingUtil;
    private final LogMapper logMapper;
    public ContractView getContractView(HttpServletRequest request, ProgressRequest param) {
        // 계약서 정보
        return contractsViewMapper.getContractView(param.getContractNo());
    }
    private String replaceWageType(String type){
        String replaceType ="";
        switch (type) {
            case "month" -> {
                replaceType =  "hó";
            }
            case "hour" -> {
                replaceType =  "óra";
            }
            default -> {
                replaceType = "";
            }
        }
        return replaceType;
    }

    public List<HistoryVO> getContractHistoryView(HttpServletRequest request, ProgressRequest param) {
        List<HistoryVO> list =  logMapper.getContractLogList(param.getContractNo());
        list.forEach(e -> {
            e.setProcessStep(LogTypeEnum.getAction(e.getProcessStep()));
            e.setFirstName(e.getCreatedByName());
            e.setLastName("");
            int index = e.getCreatedByName().indexOf(" ");
            if (index != -1) {
                try {
                    String lastName = StringUtil.getSubstring(e.getCreatedByName(), 0, index);
                    String firstName = StringUtil.getSubstring(e.getCreatedByName(), index);

                    e.setFirstName(firstName);
                    e.setLastName(lastName);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        return  list;
    }
}

