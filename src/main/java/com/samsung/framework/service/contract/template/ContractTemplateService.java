package com.samsung.framework.service.contract.template;

import com.samsung.framework.common.enums.ContractTemplateEnum;
import com.samsung.framework.mapper.contract.template.ContractTemplateMapper;
import com.samsung.framework.vo.account.AccountVO;
import com.samsung.framework.vo.contract.template.ContractTemplateVO;
import com.samsung.framework.vo.contract.template.Template;
import com.samsung.framework.vo.search.SearchVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContractTemplateService {

    private final ContractTemplateMapper contractTemplateMapper;

    /**
     * 계약서 템플릿 목록 조회
     *
     * @return
     */
    public List<ContractTemplateVO> getContractTemplateList(SearchVO searchVO) {
        return contractTemplateMapper.getContractTemplateList(searchVO);
    }
    /**
     *  계약서 템플릿 목록 개수 조회
     *
     * */
    public int getContractTemplateListCount(SearchVO searchVO) {
        return contractTemplateMapper.getContractTemplateListCount(searchVO);
    }

    public ContractTemplateVO getContractTemplateInfo(String seq) {
        ContractTemplateVO info = contractTemplateMapper.getContractTemplateInfo(seq);
        info.setTemplateType(ContractTemplateEnum.getTemplateTitle(info.getTemplateType()));
        return info;
    }

    public int saveContractTemplateInfo(ContractTemplateVO contractTemplateVO , HttpServletRequest request) {
        // updated by
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        contractTemplateVO.setUpdatedBy(account.getAdminId());
        return contractTemplateMapper.saveContractTemplateInfo(contractTemplateVO);
    }

    public Map<String,Object> saveContractTemplateCopyInfo(ContractTemplateVO contractTemplateVO , HttpServletRequest request) {
        Map<String ,Object> resultMap = new HashMap<String ,Object>();
        //  로그인 한  관리자 정보
        HttpSession session = request.getSession();
        AccountVO account = (AccountVO) session.getAttribute("loginInfo");
        contractTemplateVO.setUpdatedBy(account.getAdminId());
        contractTemplateVO.setCreatedBy(account.getAdminId());

        int result = contractTemplateMapper.saveContractTemplateCopyInfo(contractTemplateVO);
        result += contractTemplateMapper.saveContractTemplateCopyDetailsInfo(contractTemplateVO);
        resultMap.put("result" , result);
        resultMap.put("templateSeq",contractTemplateVO.getNextSeq());
        return resultMap;
    }

}
