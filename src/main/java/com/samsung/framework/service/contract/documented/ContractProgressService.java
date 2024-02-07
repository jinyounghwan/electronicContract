package com.samsung.framework.service.contract.documented;

import com.samsung.framework.common.enums.ContractProcessEnum;
import com.samsung.framework.common.enums.LogTypeEnum;
import com.samsung.framework.common.enums.ResultCodeMsgEnum;
import com.samsung.framework.common.utils.LogUtil;
import com.samsung.framework.common.utils.StringUtil;
import com.samsung.framework.domain.contract.ProgressRequest;
import com.samsung.framework.domain.log.LogSaveRequest;
import com.samsung.framework.mapper.contract.documented.ContractProgressMapper;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.common.ResultStatusVO;
import com.samsung.framework.vo.contract.creation.ContractVO;
import com.samsung.framework.vo.log.LogSaveResponse;
import com.samsung.framework.vo.search.SearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractProgressService {
    private final ContractProgressMapper contractProgressMapper;
    private final LogUtil logUtil;
    public int getContractProgressTotal(SearchVO searchVO) {
        return contractProgressMapper.getContractProgressTotal(searchVO);
    }

    public List<ContractVO> getContractProgressList(SearchVO searchVO) {

        List<ContractVO> list = contractProgressMapper.getContractProgressList(searchVO);
        list.forEach(e -> {e.setDocStatus(ContractProcessEnum.getProcessStatus(e.getDocStatus()));
            e.setProcessStatus(ContractProcessEnum.getProcessStatus(e.getProcessStatus()));
        });
        return list;
    }

    public ResultStatusVO updateContractRecall(List<ProgressRequest> progressRequest ,HttpServletRequest request) {
        // created by
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        progressRequest.forEach(el ->{ el.setDocStatus(ContractProcessEnum.processCode(ContractProcessEnum.RECALLED));
                                       el.setUpdatedBy(account.getAdminId());
                                       int result = contractProgressMapper.updateContractDocStatusInfo(el);
                                       if(result > 0){
                                           // 저장이 성공 되었을 때
                                           this.saveLogs(request,LogTypeEnum.LOG_RECALL , el);
                                       }
        });

        return new ResultStatusVO();
    }

    public ResultStatusVO updateContractAssign(List<ProgressRequest> list , HttpServletRequest request) {
        // created by
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        list.forEach(el ->{ el.setDocStatus(ContractProcessEnum.processCode(ContractProcessEnum.ASSIGNED));
            el.setUpdatedBy(account.getAdminId());
            int result = contractProgressMapper.updateContractDocStatusInfo(el);
            if(result > 0){
                this.saveLogs(request,LogTypeEnum.LOG_ASSIGN , el);
            }
        });

        return new ResultStatusVO();
    }

    public ContractVO getContractProgressInfo(String seq) {
        ContractVO contractVO =  contractProgressMapper.getContractProgressInfo(seq);
        contractVO.setDocStatus(ContractProcessEnum.getProcessStatus(contractVO.getDocStatus()));
        contractVO.setProcessStatus(ContractProcessEnum.getProcessStatus(contractVO.getProcessStatus()));
        return contractVO;
    }

    public ResultStatusVO updateRecallInfo(ProgressRequest progressRequest ,HttpServletRequest request) {
        progressRequest.setDocStatus(ContractProcessEnum.processCode(ContractProcessEnum.RECALLED));
        int result = contractProgressMapper.updateContractDocStatusInfo(progressRequest);
        if(result == 0 ){
            return new ResultStatusVO(ResultCodeMsgEnum.UPDATE_DATA_FAIL.getCode(), ResultCodeMsgEnum.UPDATE_DATA_FAIL.getMsg());
        }
        this.saveLogs(request,LogTypeEnum.LOG_RECALL , progressRequest);
        return new ResultStatusVO();
    }

    public ResultStatusVO updateAssignInfo(ProgressRequest progressRequest , HttpServletRequest request) {
        progressRequest.setDocStatus(ContractProcessEnum.processCode(ContractProcessEnum.ASSIGNED));
        int result = contractProgressMapper.updateContractDocStatusInfo(progressRequest);
        if(result == 0 ){
            return new ResultStatusVO(ResultCodeMsgEnum.UPDATE_DATA_FAIL.getCode(), ResultCodeMsgEnum.UPDATE_DATA_FAIL.getMsg());
        }
        this.saveLogs(request,LogTypeEnum.LOG_ASSIGN , progressRequest);
        return new ResultStatusVO();
    }

    private void saveLogs(HttpServletRequest request , LogTypeEnum type , ProgressRequest progress){
        // created by
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        // 저장이 성공 되었을 때
        LogSaveRequest saveRequest = LogSaveRequest.builder().logType(type)
               // .processStep(type.getDescription())
                .ipAddress(request.getRemoteAddr() + ":" + request.getRemotePort())
                .createdBy(account.getAdminId())
                .contractNo(StringUtil.getString(progress.getContractNo()))
                .build();
        Map<String, LogSaveResponse> logs = logUtil.saveLog(saveRequest);
    }

}
